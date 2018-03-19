'use strict';

interior.controller('mainController', ['$scope', '$http', '$document', function ($scope, $http, $document) {

    $http.get('/products').success(function(result) {
        for (var i = 0; i < result.length; i++) {
            var product = result[i];
            product.quantity = 1;
            product.img = product.img ? product.img : "img/" + product.id + ".jpg"
        }
        $scope.products = result;
    }).error(function(error) {
        $scope.products = [];
    });

    $scope.user = (localStorage.user && localStorage.user != "null") ? JSON.parse(localStorage.getItem("user")) : {};

    $scope.login = {};

    $scope.currentProduct = {};
    $scope.cartItems = [];
    $scope.cartCount = 'пусто';
    $scope.totalPrice = 0;
    $scope.message = "";
    $scope.newProduct = {};

    $scope.getProducts = function(){
        $http.get('/products').success(function(result) {
            for (var i = 0; i < result.length; i++) {
                var product = result[i];
                product.quantity = 1;
                product.img = product.img ? product.img : "img/" + product.id + ".jpg"
            }
            $scope.products = result;
        }).error(function(error) {
            $scope.products = [];
        });
    };

    $scope.addToCart = function(product){
        product.totalCurrentPrice = parseInt(product.currentPrice) * parseInt(product.quantity);
        $scope.cartItems.push(product);
        $scope.cartCount = $scope.cartItems.length;
        $scope.countTotalPrice();
    };

    $scope.removeCartItem = function(index){
        $scope.cartItems.splice(index, 1);

        if($scope.cartItems.length){
            $scope.cartCount = $scope.cartItems.length;
        } else{
            $scope.cartCount = 'пусто';
        }

        $scope.countTotalPrice();
    };

    $scope.changeTotalCurrentPrice = function(product, index){
        product.totalCurrentPrice = parseInt(product.currentPrice) * parseInt(product.quantity);
        $scope.countTotalPrice();
    };

    $scope.countTotalPrice = function(){
        $scope.totalPrice = 0;
        for (var i = 0; i < $scope.cartItems.length; i++){
            $scope.totalPrice = $scope.totalPrice + $scope.cartItems[i].totalCurrentPrice;
        }
    };

    $scope.getSale = function(){
        var sale = null;
        if ($scope.newProduct && $scope.newProduct.isSale && $scope.newProduct.currentPrice){
            sale = 100 - ($scope.newProduct.currentPrice * 100 / $scope.newProduct.oldPrice);
        }
        $scope.newProduct.sale = sale + "%";
    };

    $scope.openProductInfo = function(id){
        var allProducts = angular.copy($scope.products),
            number = parseInt(id);
        allProducts.forEach(function (item){
            if(item.id === number){
                $scope.currentProduct = item;
            }
        });
    };

    $scope.selectedProduct = function(row) {
        if (row){
            $scope.openProductInfo(row.originalObject.id);
        }
    };

    $scope.orderBy = function(info){
        info.cartItems = $scope.cartItems;

        $http.post('/order', info).
            success(function(data) {
                console.log(data);
            }).
            error(function(data) {
                console.log(data);
            });
    };

    $scope.logout = function (){
        $scope.user = {};
        $scope.login = {};
        localStorage.setItem("user", null);
    };

    $scope.authorization = function (data){
        $scope.message = "";
        var users = [];

        $http.get('/users').success(function(result) {
            var users = result;
            for (var i = 0; i < users.length; i++){
                var user = users[i];
                if(user.email === data.email){
                    if(user.password === data.password) {
                        $scope.user = user;
                        localStorage.setItem("user", JSON.stringify(user));
                        angular.element('#authorization').modal('hide');
                        return;
                    } else {
                        $scope.message = "Неправильный пароль.";
                        return;
                    }
                }
            }
            $scope.message = "Аккаунт не найден. Пройдите регистрацию!";

        }).error(function(error) {
            $scope.message = "Сервер недоступен. Извините!";
        });
    };

    $scope.registration = function (data){
        $scope.message = "";

        data.role = "quest";
        $http.post('/users', data).success(function(result) {
                $http.get('/users').success(function(result) {
                    var users = result;
                    for (var i = 0; i < users.length; i++){
                        var user = users[i];
                        if(user.email === data.email){
                            $scope.user = user;
                            localStorage.setItem("user", JSON.stringify(user));
                            angular.element('#registration').modal('hide');
                        }
                    }
                }).error(function(error) {
                    $scope.message = "Сервер недоступен. Извините!";
                });
            }).error(function(error) {
                $scope.message = "Аккаунт с таким email уже существует!";
            });
    };

    $scope.addNewProduct = function (data){
        $scope.message = "";
        var newProduct = {
            name: data.name,
            description: data.description,
            img: data.img,
            oldPrice: data.oldPrice,
            currentPrice: data.isSale ? data.currentPrice : null,
            sale: data.isSale ? data.sale.replace("%","") : null,
            tag: data.tag
        };

        $http.post('/products', newProduct).success(function(result) {
            $scope.message = "Товар добавлен!";
            $scope.newProduct = {};
            $scope.newProduct.isSale = false;
        }).error(function(error) {
            $scope.message = "Сервер недоступен. Извините!";
        });
    }

}]);

