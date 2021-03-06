///<reference path="../libs/jqueryui/jqueryui.d.ts"/>
///<amd-dependency path="jquery.ui"/>
///<amd-dependency path="jqrangeslider"/>
///<reference path="../libs/jqueryui/jqrangeslider.d.ts"/>
///<reference path="../libs/knockout/knockout.d.ts"/>
///<reference path="./BrowseNode.ts"/>
import $ = require('jquery');
import ko = require('knockout');

function setupBindings() {
    ko.bindingHandlers['slider'] = {
        init: function(element, valueAccessor) {
            // First get the latest data that we're bound to
            var value = valueAccessor();
            var valueUnwrapped: any = ko.utils.unwrapObservable(value);
            $(element).rangeSlider(valueUnwrapped);
            $(element).bind("valuesChanging", function (e, data) {
                valueUnwrapped.slide(e, data);
            });
        }
    };

    ko.bindingHandlers['slide'] = {
        update: function(element, valueAccessor, allBindingsAccessor) {
            var shouldShow = ko.utils.unwrapObservable(valueAccessor());
            var bindings = allBindingsAccessor();
            var direction = ko.utils.unwrapObservable(bindings.direction);
            var duration = ko.utils.unwrapObservable<number>(bindings.duration) || 400;
            var after: Function = ko.utils.unwrapObservable<Function>(bindings.after);

            var effectOptions = { "direction": direction };

            if (shouldShow) {
                after();
                $(element).show("slide", effectOptions, duration);
            } else {
                $(element).hide("slide", effectOptions, duration, after);
            }

        }
    };

    ko.bindingHandlers['stackdrag'] = {
        init: function(element, valueAccessor) {
            var thisLevel = $(element).parent().children();
            var value = valueAccessor();
            var valueUnwrapped = ko.utils.unwrapObservable(value);

            $.each(thisLevel, function(index, e) { $(e).draggable(valueUnwrapped); });
        }
    };

    ko.bindingHandlers['legendBlock'] = {
        init: function(element, valueAccessor) {
            // First get the latest data that we're bound to
            var value = valueAccessor();

            // Next, whether or not the supplied model property is observable, get its current value
            var valueUnwrapped = ko.utils.unwrapObservable(value);

            var context = element.getContext('2d');

            context.beginPath();
            context.rect(3, 3, element.width - 6, element.height - 6);
            context.fillStyle = valueUnwrapped;
            context.fill();
            context.lineWidth = 2;
            context.strokeStyle = '#F0F0F0';
            context.stroke();
        },
        update: function(element, valueAccessor) {
            // First get the latest data that we're bound to
            var value = valueAccessor();

            // Next, whether or not the supplied model property is observable, get its current value
            var valueUnwrapped = ko.utils.unwrapObservable(value);

            var context = element.getContext('2d');
            context.clearRect(0, 0, element.width, element.height);
            context.beginPath();
            context.rect(3, 3, element.width - 6, element.height - 6);
            context.fillStyle = valueUnwrapped;
            context.fill();
            context.lineWidth = 2;
            context.strokeStyle = '#F0F0F0';
            context.stroke();
        }
    };
}

export = setupBindings;
