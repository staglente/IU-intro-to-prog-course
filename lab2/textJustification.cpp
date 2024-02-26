#include <stdio.h>
#include <string.h>
#include <assert.h>
int main(){
    /* open files to i/o data */
    FILE *fin = fopen("input.txt", "r");
    assert(fin);
    FILE *fout = fopen("output.txt", "w");
    assert(fout);
    /* work with input data */
    char s[3500]; // input string
    int maxw; // input max width
    fgets(s, sizeof(s), fin);
    fscanf(fin, "%d", &maxw);
    /* create helpful entities */
    char w[3500][250]; // array of all words in the input string
    int cnt = 0, si = 0, cur_i = 0; // current index of the word, current index of the char in the input string,
    // and index of the current char in current word

    /* fill array of all words from the input string */
    int last_sp = 0; // shows if there are some continuous spacebars or not
    while(si < strlen(s)){
        if(s[si] == ' ' && !last_sp){
            cnt++;
            cur_i = 0;
            last_sp = 1;
        }
        else if(s[si] != ' ' && s[si] != '\n'){
            w[cnt][cur_i] = s[si];
            cur_i++;
            last_sp = 0;
        }
        si++;
    }
    /* main part */
    int l = 0, r = 0; // two indexes points on the first word and the last word which can be used at the same line
    while(l <= cnt){
        int curlen = 0; // length of cur line
        while(r <= cnt && curlen + strlen(w[r]) + (l != r) <= maxw){
            curlen += strlen(w[r]) + (l != r); // update current line length by addition of the current word and
            // spacebar if it is necessary
            r++;
        }
        r--; // move "pointer" for the last usable word instead of the first non-used
        int sp = 0, sp_extra = 0; // if it is one word in the line then no spacebars are needed
        if(r - l != 0){
            sp = (maxw - curlen) / (r - l); // amount of spacebars between two words
            sp_extra = maxw - curlen - sp * (r-l); // amount of addition spacebars after the first word in line
        }
        /* output data */
        for(; l <= r; l++){
            if(r == cnt){
                l == r ? fprintf(fout, "%s", w[l]) : fprintf(fout, "%s ", w[l]); // left-justification
            }
            else{
                fprintf(fout, "%s ", w[l]);
                for(int i = 0; i < (sp_extra > 0) + sp; i++)
                    fprintf(fout, " ");
                sp_extra--;
            }
        }
        fprintf(fout, "\n");
        r = l; // move our "pointers" to the first non-used word
    }
    fclose(fin);
    fclose(fout);
}