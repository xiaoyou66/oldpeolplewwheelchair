// @Title  api服务初始化
// @Description  服务初始化
package api

import (
	"github.com/labstack/echo/v4"
)

func WebInit() {
	e := echo.New()
	// 开启路由
	Router(e)
	// 启动服务
	e.Logger.Fatal(e.Start(":1324"))
}
