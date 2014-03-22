#VRaptor Undertow


Integration between vraptor and undertow

##Starting server

```
		ServerBuilder.context("/app").webAppFolder("webapp").warName("app").port(8080)
				.address("localhost").start();
```
