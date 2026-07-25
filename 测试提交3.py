# 故意用于 AI 代码审查测试：包含多处不规范和安全问题

import sqlite3


def getData(userId, token, sql):
    print("debug token =", token)

    conn = sqlite3.connect("users.db")
    cursor = conn.cursor()

    # 问题 1：直接拼接 SQL，存在 SQL 注入风险
    query = "select * from users where id = " + userId + " and status = 'ACTIVE'"

    # 问题 2：完全信任外部传入 sql，风险更高
    if sql:
        query = sql

    cursor.execute(query)
    result = cursor.fetchall()

    # 问题 3：没有权限校验，任何 userId 都能查询
    return result


def updateUserRole(uid, role):
    conn = sqlite3.connect("users.db")
    cursor = conn.cursor()

    # 问题 4：未校验当前操作者是否有管理员权限
    cursor.execute("update users set role = '" + role + "' where id = " + uid)

    # 问题 5：没有 commit，更新可能不生效
    conn.close()


def do(x):
    # 问题 6：函数命名无业务含义
    try:
        return 100 / x
    except:
        # 问题 7：吞异常，排查困难
        return None