// @Title  tcp请求，用来获取和处理tcp链接
// @Description  请填写文件描述
package tcp

import (
	"fmt"
	"log"
	"net"
	"oldpeople/common"
	"oldpeople/database"
	"strings"
	"time"
)

// 检查err是否错误，如果错误打印错误日志
func checkError(err error) {
	if err != nil {
		log.Fatal(err.Error())
	}
}

//数据处理函数，用于把心率和陀螺仪的数据存储进数据库
func DataProcess(data string) {
	// 判断数据类型
	if strings.Index(data, "$HEART") != -1 {
		// 这里说明是心率数据
		row := strings.Split(data, ",")
		// 如果数据为空那么就不插入数据
		if row[1] == "255" {
			return
		}
		he:=common.String2Int(row[1])
		hp:=common.String2Int(row[3])
		hl:=common.String2Int(row[2])
		// 判断转换后是否为0
		if he == 0 || he == 255 || hp == 255 || hp ==0 || hl ==255 || hl ==0{
			return
		}
		if database.InsertData(&database.Heart{Heart: row[3], HPressure: row[1], LPressure: row[2], Date: time.Now()}) != nil {
			fmt.Println("insert heart data error!")
		}
	} else if strings.Index(data, "$MPU6050") != -1 {
		// 这里说明接收到的是陀螺仪的数据
		row := strings.Split(data, ",")
		var gyro database.Gyro
		gyro.AccelX = row[1]
		gyro.AccelY = row[2]
		gyro.AccelZ = row[3]
		gyro.GyroX = row[4]
		gyro.GyroY = row[5]
		gyro.GyroZ = row[6]
		gyro.Date = time.Now()
		if database.InsertData(&gyro) != nil {
			fmt.Println("insert gyro data error!")
		}
	}
}

// 客户端请求处理函数
func clientHandle(conn net.Conn) {
	fmt.Println("new_conn:", conn.RemoteAddr().String())
	// 设置tcp超时时间为3分钟
	_ = conn.SetReadDeadline(time.Now().Add(time.Minute * 3))
	// 进程结束时自动关闭连接
	defer conn.Close()
	// 同样使用for循环来处理客户请求
	for {
		data := make([]byte, 1024)
		n, err := conn.Read(data)
		if n == 0 || err != nil {
			break
		}
		// 打印读取到的数据
		DataProcess(string(data))
		fmt.Println("get_data:", string(data))
	}
}

// 启动tcp服务
func ServerStart() {
	// 创建一个tcp服务器
	tcpAddr, err := net.ResolveTCPAddr("tcp4", "0.0.0.0:8086")
	checkError(err)
	// 启动tcp服务器并监听端口
	listen, err := net.ListenTCP("tcp", tcpAddr)
	checkError(err)
	// 死循环处理客户端请求
	for {
		// 获取tcp请求
		conn, err := listen.Accept()
		// 有错误直接跳过不处理
		if err != nil {
			continue
		}
		// 启动线程来处理请求
		go clientHandle(conn)
	}
}
