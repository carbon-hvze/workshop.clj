.EXPORT_ALL_VARIABLES:
.PHONY: test build

repl:
	clj -A:dev -m "tools.repl" -p 3002

test:
	clj -A:dev -m "tools.test"

clj:
	clj -m "tools.build" "target/app.jar"
