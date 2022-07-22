define({ "api": [
  {
    "type": "get",
    "url": "/appversion/latest",
    "title": "获取 App 最新版本",
    "version": "1.0.0",
    "name": "appVersionLatest",
    "group": "APP版本管理",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "platform",
            "description": "<p>Query参数, Android或iOS</p>"
          },
          {
            "group": "Parameter",
            "type": "Long",
            "optional": false,
            "field": "versionCode",
            "description": "<p>Query参数，版本标识</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /appversion/latest?platform=Android&versionCode=20200617 HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "platform",
            "description": "<p>Android或iOS</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "downloadUrl",
            "description": "<p>安装包下载地址</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "version",
            "description": "<p>版本号</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "versionCode",
            "description": "<p>版本标识</p>"
          },
          {
            "group": "返回结果",
            "type": "Boolean",
            "optional": false,
            "field": "forceUpgrade",
            "description": "<p>是否强制更新</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "releaseNote",
            "description": "<p>版本描述</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\t\"code\": 10000,\n\t\"msg\": \"success\",\n\t\"data\": {\n\t\t\"platform\": \"Android Q\",\n\t\t\"downloadUrl\": \"http://www.baidu.com\",\n\t\t\"version\": \"2.1.1\",\n\t    \"versionCode\": 20200617,\n\t\t\"forceUpgrade\": true,\n\t\t\"releaseNote\": \"版本描述\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/appversion/controller/AppVersionController.java",
    "groupTitle": "APP版本管理"
  },
  {
    "type": "post",
    "url": "/appversion/publish",
    "title": "发布版本",
    "version": "1.0.0",
    "name": "createAppVersion",
    "group": "APP版本管理",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "platform",
            "description": "<p>Android 或 iOS</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "downloadUrl",
            "description": "<p>安装包下载地址</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "version",
            "description": "<p>供客户端展示的版本名称，例如: 1.0.0</p>"
          },
          {
            "group": "Parameter",
            "type": "Long",
            "optional": false,
            "field": "versionCode",
            "description": "<p>版本标识</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "forceUpgrade",
            "description": "<p>是否强制更新</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "releaseNote",
            "description": "<p>版本描述</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /appversion/publish HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\nAuthorization: authorization\n{\n     \"platform\": \"Android\",\n     \"downloadUrl\": \"http://www.baidu.com\",\n     \"version\": \"2.1.1\",\n     \"versionCode\": 202006171047,\n     \"forceUpgrade\":true,\n     \"releaseNote\":\"版本描述\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/appversion/controller/AppVersionController.java",
    "groupTitle": "APP版本管理"
  },
  {
    "type": "delete",
    "url": "/appversion",
    "title": "删除版本",
    "version": "1.0.0",
    "name": "deleteAppVersion",
    "group": "APP版本管理",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "platform",
            "description": "<p>Query参数，Android 或 iOS</p>"
          },
          {
            "group": "Parameter",
            "type": "Long",
            "optional": false,
            "field": "versionCode",
            "description": "<p>Query参数，版本号</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "DELETE /appversion/appversion?platform=Android&versionCode=20200617 HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\nAuthorization: authorization",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/appversion/controller/AppVersionController.java",
    "groupTitle": "APP版本管理"
  },
  {
    "type": "put",
    "url": "/appversion/update",
    "title": "更新版本信息",
    "version": "1.0.0",
    "name": "updateAppVersion",
    "group": "APP版本管理",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "platform",
            "description": "<p>Query参数，Android 或 iOS，查询条件不允许修改</p>"
          },
          {
            "group": "Parameter",
            "type": "Long",
            "optional": false,
            "field": "versionCode",
            "description": "<p>Query参数，版本号，查询条件不允许修改</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "downloadUrl",
            "description": "<p>安装包下载地址</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "forceUpgrade",
            "description": "<p>是否强制更新</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "releaseNote",
            "description": "<p>版本描述</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /appversion/appversion/update?platform=Android&versionCode=2.1.1 HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\nAuthorization: authorization\n{\n     \"downloadUrl\": \"http://www.baidu.com\",\n     \"forceUpgrade\": true,\n     \"releaseNote\":\"版本描述\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/appversion/controller/AppVersionController.java",
    "groupTitle": "APP版本管理"
  },
  {
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "varname1",
            "description": "<p>No type.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "varname2",
            "description": "<p>With type.</p>"
          }
        ]
      }
    },
    "type": "",
    "url": "",
    "version": "0.0.0",
    "filename": "D:/工作项目/rongRTC/rongrtc-server/docs/apidoc/main.js",
    "group": "D:\\工作项目\\rongRTC\\rongrtc-server\\docs\\apidoc\\main.js",
    "groupTitle": "D:\\工作项目\\rongRTC\\rongrtc-server\\docs\\apidoc\\main.js",
    "name": ""
  },
  {
    "type": "post",
    "url": "/mic/room/gift/add",
    "title": "赠送礼物",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "addRoomGift",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "giftId",
            "description": "<p>礼物id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "toUid",
            "description": "<p>接收人Id</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "num",
            "description": "<p>数量</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/gift/add HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\" : \"xxxx\",\n\"giftId\" : 1,\n\"toUid\":\"xxxx\",\n\"num\":10\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/{roomId}/gift/list",
    "title": "赠送礼物列表",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "addRoomGiftList",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/{roomId}/gift/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\" : [ {\n\"userid1\" : 240\n} ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/music/add",
    "title": "添加音乐",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "addRoomMusic",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>音乐名称</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "author",
            "description": "<p>作者</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室id</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "type",
            "description": "<p>1 本地添加 2 从官方添加</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "url",
            "description": "<p>音乐url</p>"
          },
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "size",
            "description": "<p>音乐大小（默认KB）</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/music/add HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"name\" : \"xxxx\",\n\"author\" : \"xxx\",\n\"roomId\" : \"xxxxx\",\n\"type\" : 0,\n\"url\" : \"xxxxx\",\n\"size\" : \"100\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>音乐id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "author",
            "description": "<p>音乐作者</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "size",
            "description": "<p>文件大小 单位 M</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "type",
            "description": "<p>0 官方  1 本地添加 2 从官方添加</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "createDt",
            "description": "<p>创建时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n\"id\" : 1,\n\"name\" : \"a\",\n\"author\" : \"b\",\n\"roomId\" : \"sdfsdf\",\n\"type\" : 0,\n\"url\" : \"1\",\n\"size\" : \"154\",\n\"createDt\" : 1621951183000,\n\"updateDt\" : 1621951185000\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/create/check",
    "title": "用户创建房间验证",
    "version": "1.0.0",
    "name": "createCheck",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/{roomId}/create/check HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。30016 未已创建</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/create",
    "title": "创建房间",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "createRoom",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>房间名称</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "themePictureUrl",
            "description": "<p>房间主题图片地址</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否私密房间  0 否 1 是</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>私密房间密码  MD5加密</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图片地址</p>"
          },
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "kv",
            "description": "<p>房间kv列表</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/create HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"name\": \"test\",\n\"themePictureUrl\": \"xxxx\",\n\"isPrivate\": 0,\n\"password\": \"md5(aaaaaaa)\",\n\"roomType\": \"1\", //房间类型：1聊天室； 2电台\n\"kv\": [{\n\"key\": \"key1\",\n\"value\": \"val1\"\n},{\n\"key\": \"key2\",\n\"value\": \"val2\"\n}]\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Inter",
            "optional": false,
            "field": "code",
            "description": "<p>10000 成功  30016 用户已创建房间</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomName",
            "description": "<p>房间名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "themePictureUrl",
            "description": "<p>房间主题图片地址</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>创建人</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否私有</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>私有密码</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>创建人名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>创建人头像</p>"
          },
          {
            "group": "返回结果",
            "type": "Inter",
            "optional": false,
            "field": "roomType",
            "description": "<p>房间类型：1聊天室 2电台</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n\"id\":10125,\n\"roomId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"roomName\": \"名称1\",\n\"themePictureUrl\": \"xxxxxx\",\n\"backgroundUrl\": \"\",\n\"isPrivate\": 0,\n\"password\": \"\",\n\"userId\": \"xxxxxxxx\",\n\"updateDt\": 1555406087939,\n\"roomType\": 1,\n\"createUser\" : {\n\"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n\"userName\" : \"融云用户3527\",\n\"portrait\" : \"\"\n}\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{id}",
    "title": "获取房间信息",
    "version": "1.0.0",
    "name": "getRoomDetail",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id (Path 参数，需替换 url 地址中的 {roomId} 变量)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/xxxxxx HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>房间号</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomName",
            "description": "<p>房间名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "themePictureUrl",
            "description": "<p>房间主题图片地址</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>创建人</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否私有</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>私有密码</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>创建人名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>创建人头像</p>"
          },
          {
            "group": "返回结果",
            "type": "Bool",
            "optional": false,
            "field": "stop",
            "description": "<p>是否暂停</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "stopEndTime",
            "description": "<p>暂停结束时间</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "currentTime",
            "description": "<p>当前时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n\"id\":23232\n\"roomId\": \"3saDkSLFMdnsseOksdakJS6\",\n\"roomName\": \"名称2\",\n\"themePictureUrl\": \"xxxxxx\",\n\"backgroundUrl\": \"\",\n\"isPrivate\": 0,\n\"password\": \"\",\n\"userId\": \"xxxxxxxx\",\n\"updateDt\": 1555406087939,\n\"stop\": false,\n\"stopEndTime\": 1555406087939,\n\"currentTime\": 1555406087939,\n\"createUser\" : {\n\"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n\"userName\" : \"融云用户3527\",\n\"portrait\" : \"\"\n}\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/list",
    "title": "获取房间列表",
    "version": "1.0.0",
    "name": "getRoomList",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "type",
            "description": "<p>房间类型 1 聊天室(默认) 2 电台</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "page",
            "description": "<p>页码</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "size",
            "description": "<p>返回记录数(Query 参数，需拼接到 url 之后)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/list?page=1&size=10 HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "totalCount",
            "description": "<p>总记录数</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>房间号</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomName",
            "description": "<p>房间名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "themePictureUrl",
            "description": "<p>房间主题图片地址</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>创建人</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否私有</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>私有密码</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>创建人名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>创建人头像</p>"
          },
          {
            "group": "返回结果",
            "type": "Integer",
            "optional": false,
            "field": "userTotal",
            "description": "<p>房间统计用户数量</p>"
          },
          {
            "group": "返回结果",
            "type": "Bool",
            "optional": false,
            "field": "stop",
            "description": "<p>是否暂停</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "stopEndTime",
            "description": "<p>暂停结束时间</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "currentTime",
            "description": "<p>当前时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n\"totalCount\": 2,\n\"rooms\": [{\n\"id\":10125,\n\"roomId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"roomName\": \"名称1\",\n\"themePictureUrl\": \"xxxxxx\",\n\"backgroundUrl\": \"\",\n\"isPrivate\": 0,\n\"password\": \"\",\n\"userId\": \"xxxxxxxx\",\n\"updateDt\": 1555406087939,\n\"stop\": false,\n\"stopEndTime\": 1555406087939,\n\"currentTime\": 1555406087939,\n\"createUser\" : {\n\"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n\"userName\" : \"融云用户3527\",\n\"portrait\" : \"\"\n},\n\"userTotal\" : 100\n}, {\n\"id\":10126,\n\"roomId\": \"3saDkSLFMdnsseOksdakJS6\",\n\"roomName\": \"名称2\",\n\"themePictureUrl\": \"xxxxxx\",\n\"backgroundUrl\": \"\",\n\"isPrivate\": 0,\n\"password\": \"\",\n\"userId\": \"xxxxxxxx\",\n\"updateDt\": 1555406087939,\n\"stop\": false,\n\"stopEndTime\": 1555406087939,\n\"currentTime\": 1555406087939,\n\"createUser\" : {\n\"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n\"userName\" : \"融云用户3527\",\n\"portrait\" : \"\"\n},\n\"userTotal\" : 100\n<p>\n}]\n},\n\"images\":[\"aaa.png\",\"bbb.png\"]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/setting",
    "title": "获取房间设置",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "getRoomSetting",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/{roomId}/setting HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"，\n\"result\": {\n\"roomId\": \"xxxxxxx\",\n\"applyOnMic\": true,   //申请上麦模式\n\"applyAllLockMic\": true,   //全麦锁麦\n\"applyAllLockSeat\": true,  //全麦锁座\n\"setMute\": true,    //静音\n\"setSeatNumber\": 8  //设置4座、8座\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/music/move",
    "title": "移动音乐",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "moveRoomMusic",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "fromId",
            "description": "<p>音乐id 放到 toId 的后面</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "toId",
            "description": "<p>音乐id</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/music/move HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\" : \"xxxxx\",\n\"fromId\" : 0,\n\"toId\":0\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/gag/members",
    "title": "查询禁言用户列表",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "queryGagRoomUsers",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id，(Path 参数，需替换 url 地址中的 {roomId} 变量)</p>"
          },
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "userIds",
            "description": "<p>用户id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/xxxxxx/gag/members HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>头像</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": [{\n\"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"userName\": \"李晓明\",\n\"portrait\": \"xxxxxxxx\"\n},\n{\n\"userId\": \"sIl1nG5AQD8h-O7A2zlN5Q\",\n\"userName\": \"张三\",\n\"portrait\": \"xxxxx\"\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/background",
    "title": "房间背景修改",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "roomBackground",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/background HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\": \"xxxxxxx\",\n\"backgroundUrl\": \"xxxxxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/delete",
    "title": "IM 删除聊天室",
    "version": "1.0.0",
    "name": "roomDelete",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室 Id。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/xxxxx/delete HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/manage",
    "title": "房间管理员设置",
    "version": "1.0.0",
    "name": "roomManage",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>人员id</p>"
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "isManage",
            "description": "<p>是否管理员</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/manage HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\": \"xxxxxxx\",\n\"userId\": \"xxxx\",\n\"isManage\": true\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/manage/list",
    "title": "房间管理员列表",
    "version": "1.0.0",
    "name": "roomManageList",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/{roomId}/manage/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n\"data\": [\n{\n\"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"userName\": \"李晓明\",\n\"portrait\": \"xxxxxxxx\"\n},\n{\n\"userId\": \"sIl1nG5AQD8h-O7A2zlN5Q\",\n\"userName\": \"张三\",\n\"portrait\": \"xxxxx\"\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/members",
    "title": "获取房间成员列表",
    "version": "1.0.0",
    "name": "roomMembers",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id (Path 参数，需替换 url 地址中的 {roomId} 变量)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/setting/xxxxxxx/members HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>头像</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": [\n{\n\"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"userName\": \"李晓明\",\n\"portrait\": \"xxxxxxxx\"\n},\n{\n\"userId\": \"sIl1nG5AQD8h-O7A2zlN5Q\",\n\"userName\": \"张三\",\n\"portrait\": \"xxxxx\"\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/music/delete",
    "title": "聊天室音乐删除",
    "version": "1.0.0",
    "name": "roomMusicDelete",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室 Id。</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "id",
            "description": "<p>音乐id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/music/delete HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\":\"xxxx\",\n\"id\":xxx\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/music/list",
    "title": "聊天室音乐列表",
    "version": "1.0.0",
    "name": "roomMusicList",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室 Id。</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "type",
            "description": "<p>聊天室类型 0 官方，1 自定义。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/music/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\":\"xxxx\",\n\"type\":0\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>音乐id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "author",
            "description": "<p>音乐作者</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>聊天室id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "size",
            "description": "<p>文件大小 单位 M</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "type",
            "description": "<p>0 官方  1 本地添加 2 从官方添加</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "createDt",
            "description": "<p>创建时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\":\n[\n{\n\"id\" : 1,\n\"name\" : \"a\",\n\"author\" : \"b\",\n\"roomId\" : \"sdfsdf\",\n\"type\" : 0,\n\"url\" : \"1\",\n\"size\" : \"154\",\n\"createDt\" : 1621951183000,\n\"updateDt\" : 1621951185000\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/name",
    "title": "房间名称修改",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "roomName",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>房间名称</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/name HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\": \"xxxxxxx\",\n\"name\": \"xxxxxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/online/created/list",
    "title": "在线房主列表",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "roomOnlineList",
    "group": "房间模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/online/created/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>房间号</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "roomName",
            "description": "<p>房间名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "themePictureUrl",
            "description": "<p>房间主题图片地址</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "backgroundUrl",
            "description": "<p>房间背景图</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "updateDt",
            "description": "<p>更新时间</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>创建人</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否私有</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>私有密码</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>创建人名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>创建人头像</p>"
          },
          {
            "group": "返回结果",
            "type": "Integer",
            "optional": false,
            "field": "userTotal",
            "description": "<p>房间统计用户数量</p>"
          },
          {
            "group": "返回结果",
            "type": "Bool",
            "optional": false,
            "field": "stop",
            "description": "<p>是否暂停</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "stopEndTime",
            "description": "<p>暂停结束时间</p>"
          },
          {
            "group": "返回结果",
            "type": "Date",
            "optional": false,
            "field": "currentTime",
            "description": "<p>当前时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n\"rooms\": [{\n\"id\":10125,\n\"roomId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n\"roomName\": \"名称1\",\n\"themePictureUrl\": \"xxxxxx\",\n\"backgroundUrl\": \"\",\n\"isPrivate\": 0,\n\"password\": \"\",\n\"userId\": \"xxxxxxxx\",\n\"updateDt\": 1555406087939,\n\"stop\": false,\n\"stopEndTime\": 1555406087939,\n\"currentTime\": 1555406087939,\n\"createUser\" : {\n\"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n\"userName\" : \"融云用户3527\",\n\"portrait\" : \"\"\n},\n\"userTotal\" : 100\n}]\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/pk",
    "title": "房间pk",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "roomPk",
    "group": "房间模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>当前房间ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "toRoomId",
            "description": "<p>pk房间ID</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "status",
            "description": "<p>pk 状态 0 开始 1 暂停，惩罚阶段（送礼物不计算了） 2 结束</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/pk HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "int",
            "optional": false,
            "field": "30018",
            "description": "<p>房间正在pk 中 无法加入pk</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/pk/info/{roomId}",
    "title": "房间pk 积分排名",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "roomPk",
    "group": "房间模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "get /mic/room/pk/xxxxx HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "int",
            "optional": false,
            "field": "30018",
            "description": "<p>房间正在pk 中 无法加入pk</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"data\": {\n\"score\": 90,\n\"userInfoList\": [\n{\n\"userId\": \"05568b81-ac7a-4276-b629-6b433c275ea3\",\n\"userName\": \"测试一\",\n\"portrait\": \"05568b81-ac7a-4276-b629-6b433c275ea3/1627984748918333.jpeg\"\n}\n]\n}\n<p>\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/private",
    "title": "房间上锁解锁",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "roomPrivate",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "isPrivate",
            "description": "<p>是否上锁</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>房间密码</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/private HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\": \"xxxxxxx\",\n\"isPrivate\": 1,\n\"password\": \"xxxxxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/setting",
    "title": "房间设置",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "roomSetting",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "applyOnMic",
            "description": "<p>申请上麦模式</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "applyAllLockMic",
            "description": "<p>全麦锁麦</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "applyAllLockSeat",
            "description": "<p>全麦锁座</p>"
          },
          {
            "group": "Parameter",
            "type": "boolean",
            "optional": false,
            "field": "setMute",
            "description": "<p>设置静音</p>"
          },
          {
            "group": "Parameter",
            "type": "integer",
            "optional": false,
            "field": "setSeatNumber",
            "description": "<p>设置4座，默认8座</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/setting HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\": \"xxxxxxx\",\n\"applyOnMic\": true,   //申请上麦模式\n\"applyAllLockMic\": true,   //全麦锁麦\n\"applyAllLockSeat\": true,  //全麦锁座\n\"setMute\": true,    //静音\n\"setSeatNumber\": 8  //设置4座、8座\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/status_sync",
    "title": "IM 聊天室状态同步",
    "version": "1.0.0",
    "name": "roomStatusSync",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "chatRoomId",
            "description": "<p>聊天室 Id。</p>"
          },
          {
            "group": "Parameter",
            "type": "String[]",
            "optional": false,
            "field": "userIds",
            "description": "<p>用户 Id 数据。</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "status",
            "description": "<p>操作状态：0：直接调用接口 1：触发融云退出聊天室机制将用户踢出（聊天室中用户在离线 30 秒后有新消息产生时或离线后聊天室中产生 30 条消息时会被自动退出聊天室，此状态需要聊天室中有新消息时才会进行同步）2：用户被封禁 3：触发融云销毁聊天室机制自动销毁</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "type",
            "description": "<p>聊天室事件类型：0 创建聊天室、1 加入聊天室、2 退出聊天室、3 销毁聊天室</p>"
          },
          {
            "group": "Parameter",
            "type": "Long",
            "optional": false,
            "field": "time",
            "description": "<p>发生时间。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/status_sync HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n[\n{\n\"chatRoomId\":\"destory_11\",\n\"userIds\":[\"gggg\"],\n\"status\":0,\n\"type\":1,\n\"time\":1574476797772\n},\n{\n\"chatRoomId\":\"destory_12\",\n\"userIds\":[],\n\"status\":0,\n\"type\":0,\n\"time\":1574476797772\n}\n]",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/gag",
    "title": "用户禁言设置",
    "description": "<p>访问权限: 只有主持人有权限操作</p>",
    "version": "1.0.0",
    "name": "roomUserGag",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "operation",
            "description": "<p>操作，add:禁言, remove:解除禁言</p>"
          },
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "userIds",
            "description": "<p>用户id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/xxxxxxx/user/gag HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\n{\n\"roomId\": \"xxxxxxx\",\n\"userIds\": [\"xxxxxxx\",\"yyyyyyy\"],\n\"operation\": \"add\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/sensitive/add",
    "title": "添加敏感词",
    "description": "<p>访问权限: 只有房主才能添加敏感词</p>",
    "version": "1.0.0",
    "name": "sensitiveAdd",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>敏感词</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/sensitive/add HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\" : \"xxxx\",\n\"name\" : \"敏感词\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/sensitive/del/{id}",
    "title": "删除敏感词",
    "description": "<p>访问权限: 只有房主才能删除敏感词</p>",
    "version": "1.0.0",
    "name": "sensitiveDel",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "id",
            "description": "<p>主键ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/sensitive/del/{id} HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "put",
    "url": "/mic/room/sensitive/edit",
    "title": "编辑敏感词",
    "description": "<p>访问权限: 只有房主才能编辑敏感词</p>",
    "version": "1.0.0",
    "name": "sensitiveEdit",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "id",
            "description": "<p>主键ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>敏感词</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "PUT /mic/room/sensitive/edit HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"id\" : 1\n\"roomId\" : \"xxxx\",\n\"name\" : \"敏感词\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/sensitive/{roomId}/list",
    "title": "敏感词列表",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "sensitiveList",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/sensitive/{roomId}/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "id",
            "description": "<p>id</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>敏感词</p>"
          },
          {
            "group": "返回结果",
            "type": "Timestamp",
            "optional": false,
            "field": "createDt",
            "description": "<p>创建时间</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": [\n{\n\"id\" : 1,\n\"name\" : \"敏感词1\",\n\"createDt\" : 1621951183000\n},\n{\n\"id\" : 2,\n\"name\" : \"敏感词2\",\n\"createDt\" : 1621951183000\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/start",
    "title": "房间恢复接口",
    "description": "<p>访问权限: 只有房主才能恢复</p>",
    "version": "1.0.0",
    "name": "startRoom",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/{roomId}/start HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "get",
    "url": "/mic/room/{roomId}/stop",
    "title": "房间暂停接口",
    "description": "<p>访问权限: 只有房主才能暂停暂停</p>",
    "version": "1.0.0",
    "name": "stopRoom",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /mic/room/{roomId}/stop HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "post",
    "url": "/mic/room/toggle/type",
    "title": "切换房间类型的接口",
    "description": "<p>访问权限: 只有房主才能切换房间类型</p>",
    "version": "1.0.0",
    "name": "toggleRoomType",
    "group": "房间模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间id</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "roomType",
            "description": "<p>类型：1聊天室； 2电台</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/toggle/type HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"roomId\" : \"xxxx\",\n\"roomType\" : 1\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "房间模块"
  },
  {
    "type": "GET",
    "url": "/file/show",
    "title": "文件下载",
    "version": "1.0.0",
    "group": "文件模块",
    "name": "fileshow",
    "parameter": {
      "fields": {
        "请求参数": [
          {
            "group": "请求参数",
            "type": "String",
            "optional": false,
            "field": "path",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例",
          "content": "path=useridxxxxx/aaa.png",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "响应结果": [
          {
            "group": "响应结果",
            "type": "Object",
            "optional": false,
            "field": "response",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "响应结果示例",
          "content": "null",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/FileController.java",
    "groupTitle": "文件模块"
  },
  {
    "type": "POST",
    "url": "/file/upload",
    "title": "文件上传",
    "version": "1.0.0",
    "group": "文件模块",
    "name": "fileupload",
    "parameter": {
      "fields": {
        "请求参数": [
          {
            "group": "请求参数",
            "type": "Object",
            "optional": false,
            "field": "file",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例",
          "content": "file=",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "响应结果": [
          {
            "group": "响应结果",
            "type": "Number",
            "optional": false,
            "field": "code",
            "description": ""
          },
          {
            "group": "响应结果",
            "type": "String",
            "optional": false,
            "field": "msg",
            "description": ""
          },
          {
            "group": "响应结果",
            "type": "Object",
            "optional": false,
            "field": "result",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "响应结果示例",
          "content": "{\"msg\":\"wDc\",\"result\":{\"useridxxxxx/aaa.png\"},\"code\":4883}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/FileController.java",
    "groupTitle": "文件模块"
  },
  {
    "type": "post",
    "url": "/mic/room/message/broadcast",
    "title": "发送聊天室广播消息",
    "version": "1.0.0",
    "name": "roomMessageBroadcast",
    "group": "消息模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "fromUserId",
            "description": "<p>发送人用户 Id。</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "objectName",
            "description": "<p>消息类型，参考融云消息类型表.消息标志；可自定义消息类型，长度不超过 32 个字符，您在自定义消息时需要注意，不要以 &quot;RC:&quot; 开头，以避免与融云系统内置消息的 ObjectName 重名。（必传）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "content",
            "description": "<p>发送消息内容，单条消息最大 128k，内置消息以 JSON 方式进行数据序列化，消息中可选择是否携带用户信息，详见融云内置消息结构详解；如果 objectName 为自定义消息类型，该参数可自定义格式，不限于 JSON。（必传）</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /mic/room/message/broadcast HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n\"fromUserId\": \"xxxxx\",\n\"objectName\": \"RC:TxtMsg\"\n\"content\":\"xxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "code",
            "description": "<p>返回码，10000 为正常。</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/room/controller/RoomController.java",
    "groupTitle": "消息模块"
  },
  {
    "type": "post",
    "url": "/user/batch",
    "title": "批量获取用户信息",
    "version": "1.0.0",
    "name": "batchGetUserInfo",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "ids",
            "description": "<p>用户id</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/batch HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\n{\n     \"userIds\":[\"xxxx\", \"xxxxxxx\"]\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户ID</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>用户头像</p>"
          },
          {
            "group": "返回结果",
            "type": "Int",
            "optional": false,
            "field": "status",
            "description": "<p>是否关注 0 未关注 1 已关注</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": [{\n     \"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n     \"userName\": \"秦时明月\",\n     \"portrait\": \"http://xxx:xxx/portrait.png\"\n     \"status\": 0\n}]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "get",
    "url": "/user/change",
    "title": "更改用户所属房间",
    "version": "1.0.0",
    "name": "changeStatus",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "roomId",
            "description": "<p>房间ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /user/change HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "get",
    "url": "/user/check",
    "title": "检查当前用户所属房间",
    "version": "1.0.0",
    "name": "checkStatus",
    "group": "用户模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /user/check HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "TRoom",
            "optional": false,
            "field": "roomInfo",
            "description": "<p>房间信息</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n     \"id\":23232\n     \"roomId\": \"3saDkSLFMdnsseOksdakJS6\",\n     \"roomName\": \"名称2\",\n     \"themePictureUrl\": \"xxxxxx\",\n     \"backgroundUrl\": \"\",\n     \"isPrivate\": 0,\n     \"password\": \"\",\n     \"userId\": \"xxxxxxxx\",\n     \"updateDt\": 1555406087939,\n     \"createUser\" : {\n     \"userId\" : \"98bde6fe-0f8d-457c-ad81-a2329be00894\",\n         \"userName\" : \"融云用户3527\",\n         \"portrait\" : \"\"\n     }\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "get",
    "url": "/user/follow/{userId}",
    "title": "用户关注/取消关注",
    "version": "1.0.0",
    "name": "follow",
    "group": "用户模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /user/follow/xxxx HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "get",
    "url": "/user/follow/list",
    "title": "用户关注列表",
    "version": "1.0.0",
    "name": "followList",
    "group": "用户模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "page",
            "description": "<p>页码</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "size",
            "description": "<p>返回记录数</p>"
          },
          {
            "group": "Parameter",
            "type": "Int",
            "optional": false,
            "field": "type",
            "description": "<p>1 我关注的 2 我的粉丝 (Query 参数，需拼接到 url 之后)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /user/follow/list HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Inter",
            "optional": false,
            "field": "status",
            "description": "<p>是否相关关注 0 否 1 是</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n    \"code\": 10000,\n    \"data\": {\n        \"total\": 1,\n        \"list\": [\n            {\n                \"uid\": \"e04fb643-2783-4de7-87c5-39cabd88b2e1\",\n                \"name\": \"融云用户3637\",\n                \"portrait\": \"\",\n                \"status\": 1\n            }\n        ]\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "get",
    "url": "/user/get/18800000000",
    "title": "根据手机号查询用户信息",
    "version": "1.0.0",
    "name": "getByMobile",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /user/get/18800000000 HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "TUser",
            "optional": false,
            "field": "userInfo",
            "description": "<p>用户信息</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n     \"uid\": \"2akJS6N5QOYsCKf5LhpgqY\",\n     \"name\": \"秦时明月\",\n     \"portrait\": \"http://xxx:xxx/portrait.png\",\n     \"mobile\":\"18800000000\",\n     \"type\":\"用户类型，0:注册用户 1:游客\",\n     \"deviceId\":\"设备ID\"，\n     “createDt”:\"创建日期\",\n     \"updateDt\":\"更新日期\",\n     \"belongRoomId\": \"所属房间ID\"\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/user/login",
    "title": "用户登录",
    "version": "1.0.0",
    "name": "userLogin",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "verifyCode",
            "description": "<p>验证码(开启短信验证时，必填)</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>头像</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "deviceId",
            "description": "<p>设备ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/login HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\n{\n     \"mobile\": \"13300000000\",\n     \"verifyCode\": \"1234\",\n     \"userName\": \"秦时明月\",\n     \"portrait\": \"http://xxx:xxx/portrait.png\",\n     \"deviceId\": \"xxxxxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户ID</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>用户头像</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "imToken",
            "description": "<p>IM 连接 token</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "authorization",
            "description": "<p>认证信息</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "type",
            "description": "<p>用户类型，1 注册用户 0 游客</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n     \"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n     \"userName\": \"秦时明月\",\n     \"portrait\": \"http://xxx:xxx/portrait.png\",\n     \"imToken\": \"xxxxxx\",\n     \"authorization\": \"xxxxxxx\",\n     \"belongRoomId\": \"所属房间ID\",\n     \"type\": 1\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/user/refreshToken",
    "title": "刷新 token",
    "version": "1.0.0",
    "name": "userRefreshToken",
    "group": "用户模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/refreshToken HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "imToken",
            "description": "<p>IM 连接 token</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\",\n     \"data\": {\n     \"imToken\": \"xxxxxx\"\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/user/sendCode",
    "title": "发送短信验证码",
    "version": "1.0.0",
    "name": "userSendCode",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "mobile",
            "description": "<p>手机号</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/sendCode HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\n{\n\"mobile\": \"13333333333\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/user/update",
    "title": "更新用户信息",
    "version": "1.0.0",
    "name": "userUPdate",
    "group": "用户模块",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "parameter": {
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/update HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\n{\n     \"userName\": \"13333333333\"\n     \"portrait\":\"aaaaa.png\"\n}\nContent-Type: application/json;charset=UTF-8",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\" : {\n     \"id\" : 23,\n     \"uid\" : \"7a482915-97cd-4f6f-85ac-e1989d1a75aa\",\n     \"name\" : \"123456\",\n     \"portrait\" : \"https://gravatar.com/avatar/6c44f7a050e1ad44b6d52484cff36895?s=400&d=robohash&r=x\",\n     \"mobile\" : \"13354541\",\n     \"type\" : 1,\n     \"deviceId\" : \"1213545\",\n     \"createDt\" : 1621597990000,\n     \"updateDt\" : 1621597992000,\n     \"belongRoomId\": \"所属房间ID\"\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/user/visitorLogin",
    "title": "游客登录",
    "version": "1.0.0",
    "name": "visitorLogin",
    "group": "用户模块",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>头像</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "deviceId",
            "description": "<p>设备ID</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /user/visitorLogin HTTP/1.1\nHost: api-cn.ronghub.com\nContent-Type: application/json;charset=UTF-8\n{\n     \"userName\": \"秦时明月\",\n     \"portrait\":\"http://xxx:xxx/portrait.png\",\n     \"deviceId\": \"xxxxxxxx\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userId",
            "description": "<p>用户ID</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>用户名称</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "portrait",
            "description": "<p>用户头像</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "imToken",
            "description": "<p>IM 连接 token</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "authorization",
            "description": "<p>认证信息</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "type",
            "description": "<p>用户类型，1 注册用户 0 游客</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n\"code\": 10000,\n\"msg\": \"success\",\n\"data\": {\n     \"userId\": \"2akJS6N5QOYsCKf5LhpgqY\",\n     \"userName\": \"秦时明月\",\n     \"portrait\": \"http://xxx:xxx/portrait.png\",\n     \"imToken\": \"xxxxxx\",\n     \"authorization\": \"xxxxxxx\",\n     \"belongRoomId\": \"所属房间ID\",\n     \"type\": 0\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/user/controller/UserController.java",
    "groupTitle": "用户模块"
  },
  {
    "type": "post",
    "url": "/feedback/create",
    "title": "添加用户评价",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "create",
    "group": "用户评价",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "isGoodFeedback",
            "description": "<p>是否好评</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "reason",
            "description": "<p>反馈说明</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "POST /feedback/create HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n     \"isGoodFeedback\": false,\n     \"reason\": \"场景功能，音质质量\"\n}",
          "type": "json"
        }
      ]
    },
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": false,
            "field": "Authorization",
            "description": "<p>Authorization 头部需要传的值</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Inter",
            "optional": false,
            "field": "code",
            "description": "<p>10000 成功</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "msg",
            "description": "<p>消息提示</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/feedback/controller/FeedBackController.java",
    "groupTitle": "用户评价"
  },
  {
    "type": "get",
    "url": "/feedback/list",
    "title": "查询用户评价",
    "description": "<p>访问权限: 只有登录用户才可访问，游客无权限访问该接口</p>",
    "version": "1.0.0",
    "name": "create",
    "group": "用户评价",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "page",
            "description": "<p>页码</p>"
          },
          {
            "group": "Parameter",
            "type": "Inter",
            "optional": false,
            "field": "size",
            "description": "<p>每页显示条数</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "请求参数示例:",
          "content": "GET /feedback/list HTTP/1.1\nHost: api-cn.ronghub.com\nAuthorization: authorization\nContent-Type: application/json;charset=UTF-8\n{\n     \"page\": 1,\n     \"size\": 20\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "返回结果": [
          {
            "group": "返回结果",
            "type": "Inter",
            "optional": false,
            "field": "code",
            "description": "<p>10000 成功</p>"
          },
          {
            "group": "返回结果",
            "type": "String",
            "optional": false,
            "field": "msg",
            "description": "<p>消息提示</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "服务响应示例:",
          "content": "{\n     \"code\": 10000,\n     \"msg\": \"success\",,\n     \"data\":{\n         \"content\" : [ {\n         \"id\" : 18,\n         \"createDt\" : 1627856389000,\n         \"userId\" : \"xxxxxxxxxx\",\n         \"type\" : 0,\n         \"good\" : false,\n         \"reason\" : \"场景功能使用流程\"\n          }]\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "D:/工作项目/rongRTC/rongrtc-server/src/main/java/cn/cn.rongcloud/mic/feedback/controller/FeedBackController.java",
    "groupTitle": "用户评价"
  }
] });
