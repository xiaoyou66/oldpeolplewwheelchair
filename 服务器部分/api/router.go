// @Title  请填写文件名称
// @Description  请填写文件描述
package api

import "github.com/labstack/echo/v4"

func Router(e *echo.Echo) {
	// 设置gps数据
	e.GET("api/v1/set/gps", SetGps)
	// 获取gps数据
	e.GET("api/v1/get/gps", GetGps)
	// 获取心率数据
	e.GET("api/v1/get/heart", GetHeart)
	// 获取心率统计数据
	e.GET("api/v1/get/recent_heart", GetRecentHeart)
}
