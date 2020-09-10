// @Title  和api相关的处理函数
// @Description  用于处理api请求
package api

import (
	"fmt"
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
	if e == "" || n == "" {
		return DisplayError(c, "数据不能为空")
	}
	if database.InsertData(&database.GPS{E: e, N: n, Date: time.Now()}) != nil {
		return DisplayError(c, "插入数据失败")
	}
	fmt.Println("插入一条数据")
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
	return DisplayOk(c, Heart{Heart: common.String2Int(data.Heart), HPressure: common.String2Int(data.HPressure), LPressure: common.String2Int(data.LPressure)}, "获取数据成功")
}
