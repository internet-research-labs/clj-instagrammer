var viz = [
  {
    "stylers": [
      { "visibility": "simplified" }
    ]
  },{
    "featureType": "administrative",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "featureType": "poi",
    "stylers": [
      { "hue": "#ff0000" },
      { "visibility": "simplified" },
      { "saturation": -100 },
      { "lightness": -30 }
    ]
  },{
    "featureType": "landscape.man_made",
    "stylers": [
      { "hue": "#00ff33" },
      { "saturation": -100 },
      { "lightness": -47 }
    ]
  },{
    "featureType": "transit",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "featureType": "water",
    "stylers": [
      { "hue": "#00ffdd" },
      { "saturation": -73 },
      { "lightness": -15 }
    ]
  },{
    "featureType": "road",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "elementType": "labels",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
  }
];

/**
 * Ping a location with a geometry shape
 * @param latlng {latlng-literal} latitutude/longitude literal {:lat ... :lng ...}
 * @param map    {GMap} google map object
 * @return undefined undefined
 */
function ping(latlng, map) {
  var circle = new google.maps.Circle({center: latlng, radius: 100});
  circle.setMap(map);
}

function pingCircle(latlng, map) {
}

function initialize() {
  var mapOptions = {
    // center: { lat: -34.397, lng: 150.644 },
    center: { lat: 40.7127, lng: -74.0059 },
    zoom: 13,
    styles: viz
  };
  
  var map = new google.maps.Map(document.getElementById('map'), mapOptions);
  var grid = new Grid(map,
                      {lat: 40.7127, lng: -74.0059},
                      {lat: 40.7137, lng: -74.0049},
                      100,
                      100);
  // grid.ping({lat: 40.7127, lng: -74.0053}, "#FFFFFF");
  grid.ping({lat: 40.7128, lng: -74.0059}, "#FFFFFF");
  // console.log(grid.entry({lat: 40.7197, lng: -74.0053}));
  // console.log(grid.nudge({lat: 40.7127, lng: -74.0053}));
}

google.maps.event.addDomListener(window, 'load', initialize);
