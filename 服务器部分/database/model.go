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

// 心率血压数据
type Heart struct {
	ID        uint      `gorm:"primaryKey"` // 数据id
	Heart     string    // 心率数据
	HPressure string    // 血压高压
	LPressure string    // 血压低压
	Date      time.Time // 数据记录时间
}

// 六轴传感器数据
type Gyro struct {
	ID     uint      `gorm:"primaryKey"` // 数据id
	AccelX string    // X轴加速度
	AccelY string    // Y轴加速度
	AccelZ string    // Z轴加速度
	GyroX  string    // X轴角速度
	GyroY  string    // Y轴角速度
	GyroZ  string    // Z轴角速度
	Date   time.Time // 数据记录时间
}
