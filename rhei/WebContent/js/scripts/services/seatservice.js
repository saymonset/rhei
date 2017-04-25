'use strict';

angular.module('ticketApp').service('SeatService',
    function SeatService($resource) {
        return $resource('services/seat/:seatId', {
            seatId: '@id'
        }, {
            query: {
                method: 'GET',
                isArray: true
            },
            book: {
                method: 'POST'
            }
        });
    });
