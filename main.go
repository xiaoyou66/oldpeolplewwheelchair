// @Title  轮椅处理函数
// @Description  请填写文件描述
package main

import (
	"oldpeople/api"
	"oldpeople/database"
	"oldpeople/tcp"
)

func main() {
	// 初始化数据库连接
	database.DbInit()
	// 启动tcp服务
	go tcp.ServerStart()
	// 启动api服务
	api.WebInit()
}
