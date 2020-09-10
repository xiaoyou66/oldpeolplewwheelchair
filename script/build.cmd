::设置代理(如果你没有代理请关闭此选项)
set HTTP_PROXY=http://127.0.0.1:27890
set HTTPS_PROXY=http://127.0.0.1:7890
SET CGO_ENABLED=0
SET GOOS=linux
SET GOARCH=amd64
go build ../main.go