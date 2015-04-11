/**
 * Creates a coordinate grid from p and q, like:
 *
 *   p-------------------+
 *   |                   |
 *   |                   |
 *   +-------------------q
 *   
 * @param p coord lat-lng coordinate
 * @param q coord lat-lng coordinate
 * @param partitions integer number of discrete blocks to make the grid ouf of
 */
function Grid(map, p, q, x_partitions, y_partitions) {
  this.map = map;

  this.grid = new Array(y_partitions);
  for (var i=0; i < this.grid.length; i++) {
    this.grid[i] = new Array(x_partitions);
  }

  if (p.lat > q.lat) {
    this.top    = p.lat;
    this.bottom = q.lat;
  } else {
    this.top    = q.lat;
    this.bottom = p.lat;
  }

  if (p.lng > q.lng) {
    this.right = p.lng;
    this.left  = q.lng;
  } else {
    this.right = q.lng;
    this.left  = p.lng;
  }

  this.x_partitions = x_partitions;
  this.y_partitions = y_partitions;
}

/**
 *
 */
Grid.prototype.nudge = function(latlng) {
  // @TODO: Fix this
  var pos = this.entry(latlng);
  var dx = this.right - this.left;
  var dy = this.top - this.bottom;
  return {
    lat: (dy) * pos.i + this.bottom,
    lng: (dx) * pos.j + this.left
  };
}

/**
 *
 */
Grid.prototype.entry = function(latlng) {
  var x = latlng.lng - this.left;
  var y = latlng.lat - this.bottom;

  var dx = this.right - this.left;
  var dy = this.top - this.bottom;

  return {
    i: Math.floor((y)/dy),
    j: Math.floor((x)/dx)
  };
};

/**
 *
 */
Grid.prototype.coord = function(i, j) {
  var dx = this.right - this.left;
  var dy = this.top - this.bottom;

  return {
    lat: dx*(0.5 + j) + this.left,
    lng: dy*(0.5 + i) + this.bottom
  };
};

/**
 *
 */
Grid.prototype.set = function(latlng, color) {
  var pos = this.entry(latlng);
  var nudged = this.nudge(latlng);
  this.grid[pos.i][pos.j] = new google.maps.Circle({center: latlng, radius: 200});
};

/**
 *
 */
Grid.prototype.get = function(latlng) {
};

/**
 *
 */
Grid.prototype.ping = function(latlng, color) {
  var entry = this.entry(latlng);
  var nudge = this.nudge(latlng);

  // console.log(entry);

  this.grid[entry.i][entry.j] = new google.maps.Circle({center: nudge, radius: 100});
  this.grid[entry.i][entry.j].setMap(this.map);
};

Grid.prototype.drawRegion = function() {

  var rectangle = new google.maps.Rectangle({
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35,
    map: this.map,
    bounds: new google.maps.LatLngBounds(
      new google.maps.LatLng(this.top, this.left),
      new google.maps.LatLng(this.bottom, this.right))
    });
};
