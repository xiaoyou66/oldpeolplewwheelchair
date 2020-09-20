/***
心率模块控制驱动，这里我们采用的是串口2
该模块主要负责获取和处理心率信息
***/
#ifndef __HEART_H
#define __HEART_H
/*串口控制函数的函数说明*/

void u2Init();
void SendData2(unsigned char dat);
void SendString2(char *s);
void getHeart();
void Startheart();
char * hex2char(char * str,unsigned char dat);
#endif