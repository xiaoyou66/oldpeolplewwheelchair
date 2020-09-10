// @Title  轮椅处理函数
// @Description  请填写文件描述
package main

import (
	"oldpeople/api"
	"oldpeople/database"
	"oldpeople/tcp"
)

// 数据库初始化
func DataBaseInit() {
	_ = database.CreateTable(database.GPS{})
	_ = database.CreateTable(database.Heart{})
	_ = database.CreateTable(database.Gyro{})
}

func main() {
	// 初始化数据库连接
	database.DbInit()
	DataBaseInit()
	// 启动tcp服务
	go tcp.ServerStart()
	// 启动api服务
	api.WebInit()
}
