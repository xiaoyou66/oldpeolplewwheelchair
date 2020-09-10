#ifndef __OLED_H
#define __OLED_H
/*串口控制函数的函数说明*/
void oledInit();
void showHeart(char *str);
void showPressH(unsigned char h[]);
void showPressL(unsigned char l[]);
void showPosition(unsigned char x[],unsigned char y[],unsigned char z[]);
#endif