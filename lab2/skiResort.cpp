#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <stdlib.h>

/* create structure for items */
struct item{
    char title[200];
    char size[1000];
    char amount[1000];
    char measure[100];
};

/* create structure for tenants */
struct tenant{
    char name[700];
    char date[15], time[15];
    struct item items[55];
    int cnt;
};

/* for usage of "tenant" instead of "struct tenant" */
typedef struct tenant tenant;

/* function which checks if year is leap or not */
int is_leap(int y){
    if(y % 100 == 0) return (y % 400 == 0);
    return (y % 4 == 0);
}

/* function which checks input name for validity */
int check_name(char str[3500]){
    int c = 0;
    while(c < strlen(str) - 1){
        if(!(str[c] >= 'a' && str[c] <= 'z') && !(str[c] >= 'A' && str[c] <= 'Z') && str[c] != ' ') return 0;
        c++;
    }
    return 1;
}

/* function which checks input date for validity */
int check_moment(char str[3500]){
    int len = 1, c = 0;
    /* if all symbols are correct type or not */
    while(c < strlen(str) - 1){
        if(c != 2 && c != 5 && c != 13 && c != 16 && !((str[c] >= '0' && str[c] <= '9') || str[c] == ' ')) return 0;
        else if((c == 2 || c == 5 || c == 13 || c == 16) && !(str[c] == '/' || str[c] == ' ' || str[c] == ':')) return 0;
        if(str[c] == ' ') len++;
        c++;
    }
    /* if values of variables are correct or not */
    if(len != 2 && strlen(str) < 20) return 0;
    char day[3] = {str[0], str[1], NULL};
    char month[3] = {str[3], str[4], NULL};
    char year[5] = {str[6], str[7], str[8], str[9], NULL};
    char hh[3] = {str[11], str[12], NULL};
    char mm[3] = {str[14], str[15], NULL};
    char ss[3] = {str[17], str[18], NULL};
    int M = strtol(month, NULL, 10);
    int D = strtol(day, NULL, 10);
    int Y = strtol(year, NULL, 10);
    if(M < 1 || M > 12) return 0;
    if(((M < 8 && M % 2 == 1) || (M > 8 && M % 2 == 0)) && D > 31) return 0;
    if(((M < 8 && M % 2 == 0) || (M > 8 && M % 2 == 1)) && D > 30) return 0;
    if(M == 2 && D > is_leap(Y) + 28) return 0;
    if(strtol(hh, NULL, 10) > 23 || strtol(mm, NULL, 10) > 59 || strtol(ss, NULL, 10) > 59) return 0;
    return 1;
}

/* function which checks input item for validity and if it is, returns amount of words in title name */
int check_item(char str[3500]){
    int len = 1, c = 0;
    while(c != strlen(str) - 1){
        if(str[c] == ' ') len++;
        c++;
    }
    if(len < 4) return 0;
    c = 0;
    int sp = 0; // words in item title
    /* check title */
    while(sp < len - 3){
        if(!(str[c] >= 'a' && str[c] <= 'z') && !(str[c] >= 'A' && str[c] <= 'Z') && str[c] != ' ') return 0;
        sp += str[c] == ' ';
        c++;
    }

    /* check size */
    int pcnt = 0; // amount of points in float number
    while(str[c] != ' '){
        if(str[c] == '.') pcnt++;
        if(pcnt > 1) return 0;
        if(str[c] != '.' && !(str[c] >= '0' && str[c] <= '9')) return 0;
        c++;
    }
    c++;

    /* check amount */
    while(str[c] != ' '){
        if(!(str[c] >= '0' && str[c] <= '9')) return 0;
        c++;
    }
    c++;

    /* check measure */
    while(c < strlen(str) - 1){
        if(str[c] == ' ' || (!(str[c] >= 'a' && str[c] <= 'z') && !(str[c] >= 'A' && str[c] <= 'Z'))) return 0;
        c++;
    }
    return (len - 3);
}

/* function which checks if all structure poles are in correct bounds or not */
int final_check(tenant T){
    if(!(strlen(T.name) >= 2 && strlen(T.name) <= 30)) return 0;
    for(int j = 0; j < T.cnt; j++){
        if(!(strlen(T.items[j].title) >= 4 && strlen(T.items[j].title) <= 15)) return 0;
        if(strlen(T.items[j].measure) < 3 || strlen(T.items[j].measure) > 5) return 0;
        char *p = "pcs", *p1 = "pair", *p2 = "pairs";
        if(strlen(T.items[j].measure) == 3){
            for(int i = 0; i < 3; i++)
                if(T.items[j].measure[i] != p[i]) return 0;
        }
        else if(strlen(T.items[j].measure) == 4){
            for(int i = 0; i < 4; i++)
                if(T.items[j].measure[i] != p1[i]) return 0;
        }
        else{
            for(int i = 0; i < 5; i++)
                if(T.items[j].measure[i] != p2[i]) return 0;
        }
        if(atoi(T.items[j].amount) > 30 || atoi(T.items[j].amount) < 1) return 0;
        if(atof(T.items[j].size) - 200 > 1e-8) return 0;
    }
    return 1;
}

int main(){
    /* open files to i/o data */
    FILE *fin = fopen("input.txt", "r");
    assert(fin);
    FILE *fout = fopen("output.txt", "w");
    assert(fout);
    /* create entities to input data */
    char w[3500]; // current string
    tenant t[10]; // array of tenants
    int cnt = 0; // current index of tenant
    int cur = 0; // current line for current tenant
    int valid = 1; // shows if input is valid or not

    /* input and check data */
    while(fgets(w, sizeof(w), fin) != NULL){
        if(w[0] == '\n'){
            t[cnt].cnt = cur - 2;
            cnt++; // goes to the next tenant
            cur = 0;
            continue;
        }
        char s[strlen(w) + 2];
        int sp = 0, v = 0, cj = 0;
        /* copying string w to s without any garbage symbols */
        for(int i = 0; i <= strlen(w); i++){
            if(w[i] == ' '){
                if(v && !sp){
                    s[cj] = w[i];
                    cj++;
                }
                sp = 1;
            }
            else{
                s[cj] = w[i];
                cj++;
                v = 1;
                sp = 0;
            }
        }

        /* fill data to the structure-type element */
        switch (cur){
            case 0:
                if(!check_name(s)) valid = 0;
                else{
                    int c = 0; // indexes of current position in name
                    /* fill the name */
                    while(c < strlen(s) - 1){
                        t[cnt].name[c] = s[c];
                        c++;
                    }
                }
                cur++;
                break;
            case 1:
                if(!check_moment(s)) valid = 0;
                else{
                    int c = 0, k = 0; // indexes of current position in date and time
                    while(s[c] != ' '){
                        t[cnt].date[c] = s[c];
                        c++;
                    }
                    c++;
                    /* fill the surname */
                    while(c < strlen(s) - 1){
                        t[cnt].time[k] = s[c];
                        c++;
                        k++;
                    }
                }
                cur++;
                break;
            default:
                if(!check_item(s)) valid = 0;
                else{
                    int ci = check_item(s);
                    int c = 0, k = 0, l = 0, m = 0; // indexes of current positions in title, size, amount and measure
                    int cc = 0; // amount of words in title name at moment
                    while(cc + (s[c] == ' ') < ci){
                        if(s[c] == ' ') cc++;
                        t[cnt].items[cur - 2].title[c] = s[c];
                        c++;
                    }
                    c++;

                    while(s[c] != ' '){
                        t[cnt].items[cur - 2].size[l] = s[c];
                        c++;
                        l++;
                    }
                    c++;
                    while(s[c] != ' '){
                        t[cnt].items[cur - 2].amount[k] = s[c];
                        c++;
                        k++;
                    }
                    c++;
                    while(c < strlen(s) - 1){
                        t[cnt].items[cur - 2].measure[m] = s[c];
                        c++;
                        m++;
                    }
                    if( (s[c] >= 'a' && s[c] <= 'z') || (s[c] >= 'A' && s[c] <= 'Z') ){
                        t[cnt].items[cur - 2].measure[m] = s[c];
                        m++;
                    }
                    if(strcmp(t[cnt].items[cur-2].amount, "1") > 0 && strcmp(t[cnt].items[cur-2].measure, "pair") == 0) t[cnt].items[cur-2].measure[m] = 's';
                }
                cur++;
                break;
        }
    }

    /* checking for validity and output result */
    if(!valid){
        fprintf(fout, "Invalid input!");
    }
    else{
        t[cnt].cnt = cur - 2;
        for(int i = 0; i <= cnt; i++){
            valid *= final_check(t[i]);
            valid *= (t[i].cnt > 0);
        }

        if(!valid){
            fprintf(fout, "Invalid input!");
        }
        else{
            for(int i = 0; i <= cnt; i++){
                fprintf(fout, "%s has rented ", t[i].name);
                for(int j = 0; j < t[i].cnt; j++){
                    fprintf(fout, "%s %s of %s of size %s", t[i].items[j].amount, t[i].items[j].measure, t[i].items[j].title, t[i].items[j].size);
                    if(j + 1 < t[i].cnt - 1) fprintf(fout, ", ");
                    else if(j + 1 == t[i].cnt - 1) fprintf(fout, " and ");
                }
                fprintf(fout, " on %s at %s.\n", t[i].date, t[i].time);
            }
        }
    }
    fclose(fin);
    fclose(fout);
}