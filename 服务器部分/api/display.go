// @Title  api返回数据处理函数
// @Description  返回api接口信息
package api

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

// api返回的格式
type Data struct {
	Code int         `json:"code"` //返回码 1为获取数据成功 0为失败
	Msg  string      `json:"msg"`  // 返回的信息
	Data interface{} `json:"data"` // 返回的数据
}

// 返回正确的结果
func DisplayOk(c echo.Context, data interface{}, msg string) error {
	return c.JSON(http.StatusOK, Data{Code: 1, Data: data, Msg: msg})
}

// 返回错误的结果
func DisplayError(c echo.Context, msg string) error {
	return c.JSON(http.StatusOK, Data{Code: 0, Data: nil, Msg: msg})
}
