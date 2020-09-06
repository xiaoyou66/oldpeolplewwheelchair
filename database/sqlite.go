// @Title  数据库逻辑处理函数
// @Description  专门负责连接和管理数据库的
package database

import (
	"fmt"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

var Db *gorm.DB

// 数据库初始化
func DbInit() {
	var err error
	// 数据库初始化
	if Db, err = gorm.Open(sqlite.Open("data/data.db"), &gorm.Config{}); err != nil {
		fmt.Println("出现错误")
		panic("failed to connect database")
	}
}

// 创建表
func CreateTable(data interface{}) error {
	return Db.AutoMigrate(data)
}

// 插入数据
func InsertData(data interface{}) error {
	return Db.Create(data).Error
}

// 读取最后一条数据
func TakeLast(data interface{}) error {
	return Db.Last(data).Error
}
