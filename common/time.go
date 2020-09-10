// @Title  请填写文件名称
// @Description  请填写文件描述
package common

import (
	"strconv"
	"time"
)

//时间转string
func Time2String(time time.Time, showHour bool) string {
	if showHour {
		return time.Format("2006-01-02 15:04:05")
	}
	return time.Format("2006-01-02")
}

// 字符串转int
func String2Int(str string) int {
	if data, err := strconv.Atoi(str); err != nil {
		return 0
	} else {
		return data
	}
}
