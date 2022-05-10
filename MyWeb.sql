create database myweb_db;
use myweb_db;

-- ------
-- 首先创建一个用户类，该类存储网站的用户
-- ------
drop table if exists user_tab;
create table   `users_tab`(
    `id`          int(11)        not null auto_increment comment '用户id',
    `phone`       varchar(255)   not null                comment '手机号',
    `username`    varchar(255)   not null                comment '用户名',
    `password`    char(10)       not null                comment '性别',
    `true_name`    varchar(255)   not null                comment '真实姓名',
    `birthday`    varchar(255)   default null            comment '生日',
    `email`       varchar(255)   default null            comment '邮箱',
    `personal`    varchar(255)   default null            comment '个人简介',
    `avatar`      varchar(255)   default null            comment '头像',
    `recent_login` int unsigned  default null            comment '最近登陆',
    primary key (`id`),
    key ind_phone_password(`phone`,`password`),
    key ind_username_password(`username`,`password`)
)engine=innodb DEFAULT CHARSET=utf8mb4;

-- ------------
-- 创建角色表（用户角色的分类：普通用户、管理员和超级会员）
-- ------------
drop table if exists `role_tab`;
create table `role_tab`(
    id          int(11)         not null auto_increment comment '角色id',
    role        varchar(255)    not null                comment '角色',
    `describe`    varchar(255)    default null            comment '描述',
    primary key (`id`),
    unique key idx_uniq_(`role`)
)engine = innodb default charset =utf8mb4;

-- --------------
-- 创建角色表（用于用户角色的分类：用户id和角色id匹配的为该角色）
-- --------------
drop table if exists `user_role_tab`;
create table `user_role_tab`(
    user_id      int(11)         not null    comment '用户id',
    role_id      int(11)         not null    comment '角色id',
    key idx_user_role_id(`user_id`,`role_id`)
)engine = innodb default charset = utf8mb4;

-- ---------------
-- 书法处理表（显示处理的相关工作：包括预处理、处理后和评分等其它部分）
-- ---------------
drop table if exists `calligraphy_tab`;
create table `calligraphy_tab`(
    id              int(11)         not null                comment '书法处理id',
    user_id         int(11)         not null                comment '上传用户id',
    pre_url         varchar(255)    not null                comment '书法图片url',
    upload_time     int unsigned    default null            comment '上传时间',
    after_url       varchar(255)    not null                comment '书法图片url',
    dispose_time    int unsigned    default null            comment '处理时间',
    remark_content  text            not null                comment '评语内容',
    score           int(2)          not null                comment '分数',
    score_detail    varchar(255)    default null            comment '打分细节',
    descri          text            not null                comment '简介内容',
    primary key (`id`)
#     key ind_pre_aft_rem(`pre_img_id`,`after_img_id`,`remark_id`)
)engine = innodb default charset = utf8mb4;

-- ---------------
-- 创建书法预处理图片表（用户上传书法图片至oss数据桶）
-- ---------------
-- ---------------
-- 书法处理后的图片表（处理后的图片存储至数据表）
-- --------------
-- ---------------
-- 书法的评语存储表
-- ---------------
-- ---------------
-- 简介(该图片处理的细节）
-- ---------------
-- -----------------
-- 分类表（对书法处理文章的分类)
-- ------------------
drop table if exists `categories_tab`;
create table `categories_tab`(
    id              int(11)         not null auto_increment comment '分类id',
    categoryName    varchar(255) comment '分类名',
    primary key (`id`)
)engine = innodb charset = utf8mb4;

-- -----------------
-- 书法点赞表
-- ------------------
drop table if exists `calligraphy_likes_tab`;
create table `calligraphy_likes_tab`(
    id              int(11)         not null auto_increment comment '分类id',
    callgraphyId    bigint          not null comment '书法id',
    likeId          int(11)         not null comment '点赞id',
    likeDate        int(11)         comment '点赞日期',
    isRead          tinyint(1)      comment '是否已读',
    primary key (`id`)
)engine = innodb charset = utf8mb4;

-- -----------------
-- 评论表
-- ------------------
drop table if exists `comment_tab`;
create table `comment_tab`(
    id              int(11)         not null auto_increment comment '分类id',
    pId             bigint          not null comment '是否为父留言',
    callgraphyId    bigint          not null comment '书法id',
    answererId      int             not null comment '评论者',
    respondentId    int             not null comment '被回复者',
    commentDate     varchar(255)    not null comment '评论日期',
    likes           int(255)        not null comment '喜欢书',
    commentContent  text            not null comment '评论内容',
    isRead          tinyint(1)      not null comment '是否已读',
    primary key (`id`)
)engine = innodb charset = utf8mb4;

-- -----------------
-- 评论表
-- ------------------
drop table if exists `comment_likes_tab`;
create table `comment_likes_tab`(
     id              int(11)         not null auto_increment comment '分类id',
     callgraphyId    bigint          not null comment '书法id',
     pId             int(11)         not null comment '评论id',
     likeid          int(11)         not null comment '喜欢者id',
     likeDate        int(11)         not null comment '日期',
     primary key (`id`)
)engine = innodb charset = utf8mb4;
