# micro-core
Basic plugin framework

It's micro
![architecture](architecture.png)

这是一个插件化的MVC微内核，包含有一个示例插件。思想来源于多年前使用的PLAY!框架的PLUGIN模块。插件化支持，提供了良好的扩展性而且不影响现有代码，我们可以基于此，将一个一个的功能模块以plugin方式实现，运行时或配置时定制系统功能。