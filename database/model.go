// @Title  结构文件
// @Description  表结构
package database

import "time"

// GPS信息
type GPS struct {
	ID   uint      `gorm:"primaryKey"` // 数据id
	N    string    // 纬度
	E    string    // 经度
	Date time.Time // 数据记录时间
}
