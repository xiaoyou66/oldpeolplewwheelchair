//****************************************
// 主程序
//****************************************
#include "reg51.h"
#include "send.h"
#include "mpu6050.h"
uchar dis[6];					//显示数字(-511至512)的字符数组


// 串口显示加速度传感器的数据
void display(int value){
	int i;
	lcd_printf(dis,value);
	for(i=0;i<6;i++)
	{
       SendData(dis[i]);
    }
}

//*******************************************************************************************************
//主程序
//*******************************************************************************************************
void main()
{ 
	//初始化串口
	uInit();
	delay(150);
	// 初始化MPU6050
	InitMPU6050();
	delay(150);
	while(1)
	{
		display(GetData(ACCEL_XOUT_H));	//显示X轴加速度
		display(GetData(ACCEL_YOUT_H));	//显示Y轴加速度
		display(GetData(ACCEL_ZOUT_H));	//显示Z轴加速度
		display(GetData(GYRO_XOUT_H));		//显示X轴角速度
		display(GetData(GYRO_YOUT_H));		//显示Y轴角速度
		display(GetData(GYRO_ZOUT_H));		//显示Z轴角速度
		SendString("\r\n");
		delay(2000);
	}
}

