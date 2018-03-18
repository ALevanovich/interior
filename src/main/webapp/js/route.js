'use strict';

interior.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'src/mainPage.html'
            })
            .when('/catalog/bathroom', {
                templateUrl: 'src/bathroomPage.html'
            })
            .when('/catalog/kitchen', {
                templateUrl: 'src/kitchenPage.html'
            })
            .when('/catalog/bedroom', {
                templateUrl: 'src/bedroomPage.html'
            })
            .when('/shops', {
                templateUrl: 'src/shopsPage.html'
            })
            .when('/contacts', {
                templateUrl: 'src/contactsPage.html'
            })
            .when('/shopping-cart', {
                templateUrl: 'src/shoppingCartPage.html'
            })
            .when('/addProduct', {
                templateUrl: 'src/addProduct.html'
            })
            .otherwise({
                templateUrl: 'src/404.html'
            });
    }]);