###Menu Items
> Create menu Item
```
curl -XPOST -H "Content-type: application/json" -d '{
    "name":"test2",
    "isDiscount":true,
    "price": 40,
    "weight":250
}' 'http://localhost:8080/menu-items'
```

> Get menu items
```
curl -XGET -H "Content-type: application/json" 'http://localhost:8080/menu-items?priceFrom=20&priceTo=5000&isDiscount=1&weightTo=1000'
```