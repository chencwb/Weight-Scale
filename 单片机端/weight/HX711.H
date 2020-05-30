#ifndef __HX711_H__
#define __HX711_H__


#include <reg52.h>
#include <intrins.h>

//IO����
sbit HX711_DOUT=P2^1; 
sbit HX711_SCK=P2^0; 

//�������߱�������
extern void Delay__hx711_us(void);
extern unsigned long HX711_Read(void);

#endif