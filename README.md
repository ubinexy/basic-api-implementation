### 作业要求

重复完成课堂上的Demo:
* 实现获取热搜事件列表的接口
* 注意先写测试



<<<<<<< HEAD
=======
* 将所有接口的返回值都替换成使用ResponseEntity
* 所有post请求都返回201,并且返回的头部带上index字段（值为创建的资源在列表中的位置：eg: 添加的热搜事件在列表中的index）
* 完成demo里的练习：get /rs/list和 get/rs/{index}接口返回的数据中不包含user字段
* 添加获取所有用户的接口: get /users，期望返回的数据格式例子: 
    ```
    [{
        "user_name": "xxxx",
        "user_age": 19,
        "user_gender": "female",
        "user_email": "xxx@xx",
        "user_phone": "1xxxxxxxxxx"
    }]
* 先写测试！！！
* hint: @JsonpProperty的使用

>>>>>>> customize-response


