.PHONY: up start stop down

up:
	docker compose -f compose.yaml up -d

start:
	docker compose -f compose.yaml start

stop:
	docker compose -f compose.yaml stop

down:
	docker compose -f compose.yaml down