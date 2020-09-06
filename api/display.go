// @Title  api返回数据处理函数
// @Description  返回api接口信息
package api

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type Data struct {
	Code int         `json:"code"`
	Msg  string      `json:"msg"`
	Data interface{} `json:"data"`
}

func DisplayOk(c echo.Context, data interface{}, msg string) error {
	return c.JSON(http.StatusOK, Data{Code: 1, Data: data, Msg: msg})
}

func DisplayError(c echo.Context, msg string) error {
	return c.JSON(http.StatusOK, Data{Code: 0, Data: nil, Msg: msg})
}
