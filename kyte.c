#include <stdio.h>
#include <string.h>
#include <stdlib.h>
// Kyte-Doolittle hydropathy index table
double kd_hydropathy[256] = {0.0};
void init_kd_table(void)
{
    kd_hydropathy['I'] = 4.5;
    kd_hydropathy['V'] = 4.2;
    kd_hydropathy['L'] = 3.8;
    kd_hydropathy['F'] = 2.8;
    kd_hydropathy['C'] = 2.5;
    kd_hydropathy['M'] = 1.9;
    kd_hydropathy['A'] = 1.8;
    kd_hydropathy['G'] = -0.4;
    kd_hydropathy['T'] = -0.7;
    kd_hydropathy['S'] = -0.8;
    kd_hydropathy['W'] = -0.9;
    kd_hydropathy['Y'] = -1.3;
    kd_hydropathy['P'] = -1.6;
    kd_hydropathy['H'] = -3.2;
    kd_hydropathy['D'] = -3.5;
    kd_hydropathy['N'] = -3.5;
    kd_hydropathy['E'] = -3.5;
    kd_hydropathy['Q'] = -3.5;
    kd_hydropathy['K'] = -3.9;
    kd_hydropathy['R'] = -4.5;
}
/**
 * @brief Calculate average Kyte-Doolittle hydropathy with sliding window
 * @param seq Protein sequence (uppercase single-letter amino acid code)
 * @param len Length of protein sequence
 * @param win Window size, must be odd, recommended value = 19
 * @param out Output array. out[i] = average hydropathy centered at position i
 */
void sliding_kd(const char *seq, int len, int win, double *out)
{
    int half, i, j;
    double sum;
    memset(out, 0, sizeof(double)*len);
    half = (win - 1)/2;
    for(i = half; i <= len - 1 - half; i++)
    {
        sum = 0.0;
        for(j = i - half; j <= i + half; j++)
        {
            unsigned char aa = (unsigned char)seq[j];
            sum += kd_hydropathy[aa];
        }
        out[i] = sum / win;
    }
}
/**
 * @brief Compute GRAVY: grand average of hydropathy for full sequence
 */
double calc_gravy(const char *seq, int len)
{
    int i;
    double sum = 0.0;
    for(i = 0; i < len; i++)
    {
        unsigned char aa = (unsigned char)seq[i];
        sum += kd_hydropathy[aa];
    }
    return sum / len;
}
/**
 * @brief Detect candidate transmembrane segments, cutoff = 1.6
 * Print continuous regions above threshold (GPCR TM candidates)
 */
void find_tm_candidate(const char *seq, int len, int win, double *hydro, double cutoff)
{
    int half, i, start, end;
    int in_tm = 0;
    half = (win-1)/2;
    start = 0;
    printf("\n===== Candidate Transmembrane Segments (cutoff=%.2f) =====\n", cutoff);
    for(i = half; i <= len-1-half; i++)
    {
        if(hydro[i] >= cutoff && !in_tm)
        {
            in_tm = 1;
            start = i - half;
        }
        else if(hydro[i] < cutoff && in_tm)
        {
            in_tm = 0;
            end = i + half;
            printf("TM candidate: residues %d ~ %d\n", start+1, end+1);
        }
    }
    if(in_tm)
    {
        end = len-1;
        printf("TM candidate: residues %d ~ %d\n", start+1, end+1);
    }
}
int main(void)
{
    char protein_seq[] = "MAKELVADVILVLVAGTALVLVLGNWVLGIA";
    int win_size, seq_len;
    double cutoff, gravy;
    double *hydro_result;
    int i;
    init_kd_table();
    win_size = 19;
    cutoff = 1.6;
    seq_len = strlen(protein_seq);
    hydro_result = (double*)malloc(sizeof(double)*seq_len);
    if(!hydro_result)
    {
        fprintf(stderr,"malloc fail\n");
        return 1;
    }
    sliding_kd(protein_seq, seq_len, win_size, hydro_result);
    gravy = calc_gravy(protein_seq, seq_len);
    printf("Protein sequence: %s\n", protein_seq);
    printf("Sequence length: %d\n", seq_len);
    printf("GRAVY (global hydropathy): %.3f\n", gravy);
    printf("Sliding window size: %d\n\n", win_size);
    printf("Pos\tAA\tAvg_KD\n");
    for(i = 0; i < seq_len; i++)
    {
        printf("%d\t%c\t%.3f\n", i+1, protein_seq[i], hydro_result[i]);
    }
    find_tm_candidate(protein_seq, seq_len, win_size, hydro_result, cutoff);
    free(hydro_result);
    hydro_result = NULL;
    return 0;
}
