// @Title  和api相关的处理函数
// @Description  用于处理api请求
package api

import (
	"github.com/labstack/echo/v4"
	"oldpeople/common"
	"oldpeople/database"
	"time"
)

// 设置gps信息
func SetGps(c echo.Context) error {
	// 获取经度和纬度
	e := c.FormValue("e")
	n := c.FormValue("n")
	if e == "" || n == "" || e == "0" || n == "0" {
		return DisplayError(c, "数据不能为空")
	}
	if database.InsertData(&database.GPS{E: e, N: n, Date: time.Now()}) != nil {
		return DisplayError(c, "插入数据失败")
	}
	return DisplayOk(c, nil, "插入数据成功")
}

// 获取gps数据
func GetGps(c echo.Context) error {
	var data database.GPS
	if database.TakeLast(&data) != nil {
		return DisplayError(c, "获取数据失败")
	}
	return DisplayOk(c, GPS{N: data.N, E: data.E, Date: common.Time2String(data.Date, true)}, "获取数据成功")
}

// 获取心率数据
func GetHeart(c echo.Context) error {
	var data database.Heart
	if database.TakeLast(&data) != nil {
		return DisplayError(c, "获取数据失败")
	}
	return DisplayOk(c, Heart{Heart: common.String2Int(data.Heart), HPressure: common.String2Int(data.HPressure), LPressure: common.String2Int(data.LPressure), Update: common.Time2String(data.Date, true)}, "获取数据成功")
}

// 获取最近的心率统计数据
func GetRecentHeart(c echo.Context) error {
	var data []database.Heart
	if database.TakeRecent(&data, 10) != nil {
		return DisplayError(c, "获取数据失败")
	}
	var hearts []Heart
	for _, v := range data {
		var heart Heart
		heart.Heart = common.String2Int(v.Heart)
		heart.LPressure = common.String2Int(v.LPressure)
		heart.HPressure = common.String2Int(v.HPressure)
		heart.Update = common.Time2String(v.Date, true)
		hearts = append(hearts, heart)
	}
	return DisplayOk(c, hearts, "获取数据成功")
}
