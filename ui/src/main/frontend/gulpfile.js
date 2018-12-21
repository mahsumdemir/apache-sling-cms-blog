var gulp = require('gulp');
var sass = require('gulp-sass');

sass.compiler = require('node-sass');

gulp.task('sass', function () {
   return gulp.src('./sass/**/*.scss')
       .pipe(sass().on('error', sass.logError))
       .pipe(gulp.dest('../resources/inital-content/static/clientlibs/blog'));
});

gulp.task('default', ['sass']);