#include <ESP8266WiFi.h>
#include <Ticker.h>

#define AP_SSID "xiaoyou" //这里改成你的wifi名字
#define AP_PSW  "1806040103"//这里改成你的wifi密码

Ticker flipper;
const uint16_t port = 8234;
const char * host = "192.168.123.95"; // ip or dns
WiFiClient client;//创建一个tcp client连接 
int lockstatue=0;//关锁状态

void setup()
{
  // 这里开始写初始化代码，只会执行一次
  WiFi.mode(WIFI_STA); //设置esp8266为wifi链接模式
  WiFi.begin(AP_SSID,AP_PSW); // 开始连接wifi
  WiFi.setAutoConnect(true);//自动连接
  WiFi.setAutoReconnect(true);//自动重连
  Serial.begin(9600); // 设置波特率为115200
  Serial.print("IP address: "); // 串口控制台打印
  Serial.println(WiFi.localIP()); // 打印ip地址0
  client.setNoDelay(false); // 设置连接不延时
  Serial.print("connecting to "); // 准备连接服务器
  Serial.println(host); // 打印服务器地址
  while(WiFi.status() != WL_CONNECTED){ // 不断检查wifi连接状态
    delay(500);
  }
  // 先连接服务器
  client.connect(host, port);
  delay(500);
}
 
void loop()
{
  // 函数主循环
  // 判断是否连接上了服务器
  if(!client.connected()){
    Serial.println("not connect host");
    // 尝试重新连接
      if(!client.connect(host, port)){
        delay(1000);
        return;
      }
  }
  
  // 判断串口是否有数据发送
  if(Serial.available()){
    //开始读取数据
    // 注意 双引号引起来的都是字符串常量，单引号引起来的都是字符常量， 我们这里需要要用单引号
    String data = Serial.readStringUntil('e');
    // 打印读取到的数据
    Serial.print("getHardData：");
    Serial.println(data);
    // 尝试向服务器发送数据
    client.println(data);
  }

  // 判断服务器是否有数据发送给我
  if(client.available()){
    // 开始读取数据
    String data = client.readStringUntil('e');
    // 打印读取到的数据
    Serial.print("getHostData：");
    Serial.println(data);
  }
}
  
