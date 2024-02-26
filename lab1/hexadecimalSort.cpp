#include <stdio.h>

void swap(int *a, int *b) {
    int t = *a;
    *a = *b;
    *b = t;
}

int main() {
    FILE *fin = fopen("input.txt", "r");
    FILE *fout = fopen("output.txt", "w");
    int arr[10000], k = 0;
    while (fscanf(fin, "%x", &arr[k]) != -1) k++;
    for (int i = 0; i < k; i++)
        for (int j = 0; j < k - i - 1; j++)
            if (arr[j] > arr[j + 1])
                swap(&arr[j], &arr[j + 1]);
    for (int i = 0; i < k; i++) fprintf(fout, "%X ", arr[i]);
}