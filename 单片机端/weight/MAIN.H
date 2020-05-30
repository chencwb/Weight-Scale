#ifndef __MAIN_H__
#define __MAIN_H__

#include <reg52.h>


sbit Buzzer = P1^5;


//函数或者变量声明
extern void Delay_ms(unsigned int n);
extern void Buzzer_Di();
extern void Get_Maopi();
extern void Get_Weight();
extern void Scan_Key();
extern unsigned char KEY_NUM;

#endif