# -*- coding: utf-8 -*
import requests
import serial
import random


# 数据处理函数，用来处理GPS数据
def data_process(data):
    # 判断天线状态
    if '$GPTXT' in data:
        # 获取天线数据(去掉换行然后根据,把字符串转换为数组)
        status = data.split(',')
        if 'OK' not in status[4]:
            print('天线未连接')
    elif '$GNRMC' in data:
        # 获取GPS定位数据
        gps = data.split(',')
        if gps[3] != '' and gps[5] != '':
            # 手动计算经纬度
            n_d = float(gps[3][0:2])
            n_f = float(gps[3][2:])
            n = float(n_d)+(float(n_f)/60)
            e_d = float(gps[5][0:3])
            e_f = float(gps[5][3:])
            e = float(e_d) + (float(e_f) / 60)
            print('经纬度:%f,%f' % (e, n))
        else:
            n = 0
            e = 0
            print(gps)
        # 发送请求给api服务器
        response = requests.get('http://122.51.134.241:1324/api/v1/set/gps', params={'e': e, 'n': n})
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
            # print('接收到的数据%s' % line)
            # 尝试进行解码
            try:
                data_process(line.decode('ascii'))
            except Exception:
                print('发送数据过程中出现错误%s' % Exception)
    except KeyboardInterrupt:
        ser.close()
