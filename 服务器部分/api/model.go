// @Title  请填写文件名称
// @Description  请填写文件描述
package api

// 返回的GPS数据
type GPS struct {
	N    string `json:"n"`
	E    string `json:"e"`
	Date string `json:"date"`
}

// 返回的心率数据
type Heart struct {
	Heart     int    `json:"heart"`
	HPressure int    `json:"h_pressure"`
	LPressure int    `json:"l_pressure"`
	Update    string `json:"update"`
}

// 返回的陀螺仪数据
type Gyro struct {
	AccelX string `json:"accel_x"`
	AccelY string `json:"accel_y"`
	AccelZ string `json:"accel_z"`
	GyroX  string `json:"gyro_x"`
	GyroY  string `json:"gyro_y"`
	GyroZ  string `json:"gyro_z"`
}
