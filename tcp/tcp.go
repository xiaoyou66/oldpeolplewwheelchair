// @Title  tcp请求，用来获取和处理tcp链接
// @Description  请填写文件描述
package tcp

import (
	"fmt"
	"log"
	"net"
	"time"
)

// 检查err是否错误，如果错误打印错误日志
func checkError(err error) {
	if err != nil {
		log.Fatal(err.Error())
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
		fmt.Println("get_data:", string(data))
	}
}

// 启动tcp服务
func ServerStart() {
	// 创建一个tcp服务器
	tcpAddr, err := net.ResolveTCPAddr("tcp4", "127.0.0.1:1324")
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
