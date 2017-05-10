/**
 * Created by Guy.Peleg on 5/10/2017.
 */

angular
    .module('HealthCheckApp')
    .factory('health_check_service', ['$http', function($http) {
        return {
            get_plugins_list: function () {
                return $http.get("health/plugins").then(function (response) {
                        return response.data;
                    }
                );
            },
            run_plugin: function (pluginName) {
            return $http.get("health/check/" + pluginName).then(function (response) {
                    return response.data;
                }
            );
            }
        }
    }])
