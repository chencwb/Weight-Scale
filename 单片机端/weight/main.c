#include "main.h"
#include "LCD1602.h"
#include "HX711.h"
#define uchar unsigned char  //无符号字符型 宏定义	变量范围0~255
#define uint  unsigned int	 //无符号整型 宏定义	变量范围0~65535


//定义变量
unsigned long HX711_Buffer = 0;  //用来存放HX711读取出来的数据
unsigned long Weight_Maopi = 0; //用来存放毛皮数据
long Weight_Shiwu = 0;          //用来存放实物重量
unsigned char Max_Value = 95;             //用来存放设置最大值


uint GapValue= 228;	   //传感器曲率


//报警值，单位是g
#define AlarmValue 100000	

//接收发送区
#define USART_MAX_RECV_LEN	40
uchar USART_RX_BUF[USART_MAX_RECV_LEN];
uint USART_RX_STA=0; 

//****************************************************
//初始化
//****************************************************
void UsartInit()
{
	SCON=0X50;			//设置为工作方式1
	TMOD=0X20;			//设置计数器工作方式2
	PCON=0X80;			//波特率加倍
	TH1=0XFa;			//计数器初始值设置，注意波特率是9600的
	TL1=0XFa;
	ES=1;						//打开接收中断
	EA=1;						//打开总中断
	TR1=1;					//打开计数器
}



void UART_SendData(char dat)
{
	ES = 0;
	SBUF = dat;
	while(!TI);
	TI = 0;
	ES = 1;
}		


//****************************************************
//主函数
//****************************************************
void main()
{
	uchar flag = 0;
	uint chazhi = 0;
	uint shiwei = 0;
	uint gewei = 0;
	uint shifenwei = 0;
	uint baifenwei = 0;

	uint qian = 0;
	uint hou = 0;

	uint pastData = 0;
	uint pastData2 = 0;
	

	Init_LCD1602();		//初始化LCD1602
	UsartInit();  //串口初始化
	LCD1602_write_com(0x80);			//设置LCD1602指针
	LCD1602_write_word("Welcome to use!");
	
	Get_Maopi();
	Get_Maopi();
	Delay_ms(2000);		 //延时2s
	Get_Maopi();
	Get_Maopi();				//称毛皮重量	//多次测量有利于HX711稳定
    LCD1602_write_com(0x01);    //清屏
    
	

	while(1)
	{
	   
	    //Scan_Key();
        Get_Weight();		

		shiwei = Weight_Shiwu%100000/10000;
		gewei = Weight_Shiwu%10000/1000;
		shifenwei = Weight_Shiwu%1000/100;
		baifenwei = Weight_Shiwu%100/10;

		qian = 100 + 10*shiwei + gewei;
		hou = 10*shifenwei + baifenwei;

		if(hou > pastData2)
		{
			chazhi = hou - pastData2;
		}else{
			chazhi = pastData2 - hou;
		}
		
		if(qian == 0 && hou < 5){
		//几乎没放东西
			//显示当前重量
			LCD1602_write_com(0x80);
        	LCD1602_write_word(" Weight=");
        	//LCD1602_write_data(Weight_Shiwu/100000 + 0x30);
        	LCD1602_write_data(Weight_Shiwu%100000/10000 + 0x30);
			LCD1602_write_data(Weight_Shiwu%10000/1000 + 0x30);
			LCD1602_write_data('.');
			LCD1602_write_data(Weight_Shiwu%1000/100 + 0x30);
 			LCD1602_write_data(Weight_Shiwu%100/10 + 0x30);
			//LCD1602_write_data(Weight_Shiwu%10 + 0x30);	
        
			LCD1602_write_word("Kg");
			UART_SendData(qian);
			UART_SendData(hou);	
			flag = 0;
		} else if(qian == pastData && chazhi<10){
			if(flag<10){
			//超过10不发送
			//显示当前重量
			LCD1602_write_com(0x80);
        	LCD1602_write_word(" Weight=");
        	//LCD1602_write_data(Weight_Shiwu/100000 + 0x30);
        	LCD1602_write_data(Weight_Shiwu%100000/10000 + 0x30);
			LCD1602_write_data(Weight_Shiwu%10000/1000 + 0x30);
			LCD1602_write_data('.');
			LCD1602_write_data(Weight_Shiwu%1000/100 + 0x30);
 			LCD1602_write_data(Weight_Shiwu%100/10 + 0x30);
			//LCD1602_write_data(Weight_Shiwu%10 + 0x30);	
        
			LCD1602_write_word("Kg");
			UART_SendData(qian);
			UART_SendData(hou);
			flag++;
			}
		}else{
			flag = 0;
					
			LCD1602_write_com(0x80);
        	LCD1602_write_word(" Weight=");
        	//LCD1602_write_data(Weight_Shiwu/100000 + 0x30);
        	LCD1602_write_data(Weight_Shiwu%100000/10000 + 0x30);
			LCD1602_write_data(Weight_Shiwu%10000/1000 + 0x30);
			LCD1602_write_data('.');
			LCD1602_write_data(Weight_Shiwu%1000/100 + 0x30);
 			LCD1602_write_data(Weight_Shiwu%100/10 + 0x30);
			//LCD1602_write_data(Weight_Shiwu%10 + 0x30);	
        
			LCD1602_write_word("Kg");
			UART_SendData(qian);
			UART_SendData(hou);
		
			pastData = qian;
			pastData2 = hou;
	
		}

		
	
		

	
        
        //超限报警
        if(Weight_Shiwu/1000 >= Max_Value || Weight_Shiwu >= AlarmValue) //超过设置最大值或者传感器本身量程最大值报警	
		{
			Buzzer = 0;	//Buzzer代表蜂鸣器提示。
		}
		else
		{
			Buzzer = 1;
		}
		//Delay_ms(100);
		
	}	
}




//****************************************************
//称重
//****************************************************
void Get_Weight()
{
	Weight_Shiwu = HX711_Read();
	Weight_Shiwu = Weight_Shiwu - Weight_Maopi;		//获取净重
	if(Weight_Shiwu >= 0)			
	{	
		Weight_Shiwu = (unsigned long)((float)(Weight_Shiwu/GapValue)*10); 	//计算实物的实际重量
	}
	else
	{
		Weight_Shiwu = 0;
	}
	
}

//****************************************************
//获取毛皮重量
//****************************************************
void Get_Maopi()
{
	Weight_Maopi = HX711_Read();	
} 


//****************************************************
//MS延时函数(12M晶振下测试)
//****************************************************
void Delay_ms(unsigned int n)
{
	unsigned int  i,j;
	for(i=0;i<n;i++)
		for(j=0;j<123;j++);
}








