/************************************************************************************
* //OLED显示的主程序，主要显示默认的设置
*
* 1. void delay(unsigned int z) -- 主函数中用于调整显示效果的延时函数,STC89C52 12MHZ z=1时大约延时1ms,其他频率需要自己计算
* 2. void main(void) -- 主函数
*
* History: none;
*
*************************************************************************************/

//#include "reg52.h"
#include "code-data.h"
#include "oled-iic.h"
#include "oled.h"

// OLED 显示屏初始化（显示默认的信息）
void oledInit(void)
{

	OLED_Init(); //OLED初始化
//		OLED_Fill(0xff); //屏全亮
//		OLED_Fill(0x00); //屏全灭
	// 显示信息
	OLED_P16x16Ch(0,0,17);
	OLED_P16x16Ch(18,0,0); //老
	OLED_P16x16Ch(35,0,1); //人
	OLED_P16x16Ch(52,0,2); //轮
	OLED_P16x16Ch(69,0,3); //椅
	OLED_P16x16Ch(86,0,4); //监
	OLED_P16x16Ch(103,0,5); //控
	//显示心跳
	OLED_P16x16Ch(0,2,18);
	OLED_P16x16Ch(18,2,6); //心
	OLED_P16x16Ch(35,2,7); //率
	OLED_P8x16Str(52,2,":000");
	//显示血压数据
	OLED_P16x16Ch(0,4,19);
	OLED_P16x16Ch(18,4,8); //血
	OLED_P16x16Ch(35,4,9); //压
	OLED_P8x16Str(52,4,"H000 L000");
	//显示陀螺仪
	OLED_P16x16Ch(0,6,20);
	OLED_P8x16Str(18,6,"000,000,000");
//		OLED_CLS();//清屏
//		Draw_BMP(0,0,128,8,BMP1);  //图片显示(图片显示慎用，生成的字表较大，会占用较多空间，FLASH空间8K以下慎用)
//		Draw_BMP(0,0,128,8,BMP2);
	
}

// 显示心率
void showHeart(unsigned char str[]){
	OLED_P8x16Str(61,2,str);
}

//显示血压
void showPress(unsigned char h[],unsigned char l[]){
	OLED_P8x16Str(61,4,h);
	OLED_P8x16Str(101,4,l);
}

////获取数组中某个字符
//char* showChar(unsigned char arr[],int positin){
//	unsigned char ch[2];
//	ch[0] = "6";
//	ch[1] = "\0";
//	return ch;
//}

//显示陀螺仪数据
void showPosition(unsigned char x[],unsigned char y[],unsigned char z[]){
	OLED_P8x16Str(18,6,x);
	// 注意单引号和双引号的区别
	OLED_P8x16Str(42,6,",");
	OLED_P8x16Str(50,6,y);
	OLED_P8x16Str(74,6,",");
	OLED_P8x16Str(82,6,z);
}
