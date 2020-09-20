# -*- coding: utf-8 -*
import requests
import serial
import random


# 数据处理函数，用来处理GPS数据
def data_process(data):
    # 判断天线状态
    if '$GPTXT' in data:
        # 获取天线数据(去掉换行然后根据,把字符串转换为数组)
        status = str.replace(data, '\r\n', '').split(',')
        # print('天线状态:%s' % status[4])
    elif '$GNRMC' in data:
        # 获取GPS定位数据
        gps = str.replace(data, '\r\n', '').split(',')
        response = requests.get('http://192.168.123.95:1324/api/v1/set/gps', params={'e': str(random.randint(0, 1000)),
                                                                              'n': str(random.randint(0, 10000))})
        # 发送请求给api服务器
        # print('gps定位数据:%s' % gps)
        print('返回结果%s' % response.content)


if __name__ == "__main__":
    # 使用串口，并设置波特率 9600
    ser = serial.Serial('/dev/ttyAMA0', 9600)
    # 判断串口是否打开
    if not ser.isOpen:
        ser.open()  # 打开串口
    # 循环获取数据
    try:
        while True:
            # 按行来读取数据，并且对字节数据进行解码，转换为字符串
            line = ser.readline()
            # 尝试进行解码
            try:
                line.decode('utf-8')
                data_process(line.decode('utf-8'))
            except Exception:
                print('发送数据过程中出现错误')
    except KeyboardInterrupt:
        ser.close()
