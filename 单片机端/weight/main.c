#include "main.h"
#include "LCD1602.h"
#include "HX711.h"
#define uchar unsigned char  //�޷����ַ��� �궨��	������Χ0~255
#define uint  unsigned int	 //�޷������� �궨��	������Χ0~65535


//�������
unsigned long HX711_Buffer = 0;  //�������HX711��ȡ����������
unsigned long Weight_Maopi = 0; //�������ëƤ����
long Weight_Shiwu = 0;          //�������ʵ������
unsigned char Max_Value = 95;             //��������������ֵ


uint GapValue= 228;	   //����������


//����ֵ����λ��g
#define AlarmValue 100000	

//���շ�����
#define USART_MAX_RECV_LEN	40
uchar USART_RX_BUF[USART_MAX_RECV_LEN];
uint USART_RX_STA=0; 

//****************************************************
//��ʼ��
//****************************************************
void UsartInit()
{
	SCON=0X50;			//����Ϊ������ʽ1
	TMOD=0X20;			//���ü�����������ʽ2
	PCON=0X80;			//�����ʼӱ�
	TH1=0XFa;			//��������ʼֵ���ã�ע�Ⲩ������9600��
	TL1=0XFa;
	ES=1;						//�򿪽����ж�
	EA=1;						//�����ж�
	TR1=1;					//�򿪼�����
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
//������
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
	

	Init_LCD1602();		//��ʼ��LCD1602
	UsartInit();  //���ڳ�ʼ��
	LCD1602_write_com(0x80);			//����LCD1602ָ��
	LCD1602_write_word("Welcome to use!");
	
	Get_Maopi();
	Get_Maopi();
	Delay_ms(2000);		 //��ʱ2s
	Get_Maopi();
	Get_Maopi();				//��ëƤ����	//��β���������HX711�ȶ�
    LCD1602_write_com(0x01);    //����
    
	

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
		//����û�Ŷ���
			//��ʾ��ǰ����
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
			//����10������
			//��ʾ��ǰ����
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

		
	
		

	
        
        //���ޱ���
        if(Weight_Shiwu/1000 >= Max_Value || Weight_Shiwu >= AlarmValue) //�����������ֵ���ߴ����������������ֵ����	
		{
			Buzzer = 0;	//Buzzer�����������ʾ��
		}
		else
		{
			Buzzer = 1;
		}
		//Delay_ms(100);
		
	}	
}




//****************************************************
//����
//****************************************************
void Get_Weight()
{
	Weight_Shiwu = HX711_Read();
	Weight_Shiwu = Weight_Shiwu - Weight_Maopi;		//��ȡ����
	if(Weight_Shiwu >= 0)			
	{	
		Weight_Shiwu = (unsigned long)((float)(Weight_Shiwu/GapValue)*10); 	//����ʵ���ʵ������
	}
	else
	{
		Weight_Shiwu = 0;
	}
	
}

//****************************************************
//��ȡëƤ����
//****************************************************
void Get_Maopi()
{
	Weight_Maopi = HX711_Read();	
} 


//****************************************************
//MS��ʱ����(12M�����²���)
//****************************************************
void Delay_ms(unsigned int n)
{
	unsigned int  i,j;
	for(i=0;i<n;i++)
		for(j=0;j<123;j++);
}








