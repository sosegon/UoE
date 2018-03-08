#include "mat.h"

#include <stdio.h>
#include <stdlib.h>
#include <cmath>

mat2::mat2(void) {}

mat2::mat2(float xx, float xy, float yx, float yy)
    : xx(xx), xy(xy), yx(yx), yy(yy) {}

mat2::mat2(const vec2& r1, const vec2& r2)
    : xx(r1.x), xy(r1.y), yx(r2.x), yy(r2.y) {}

mat2 mat2::Id(void) { return mat2(1, 0, 0, 1); }

mat2 mat2::Zero(void) { return mat2(0, 0, 0, 0); }

mat2 mat2::Rotation(float a) {
  return mat2(cos(a), -sin(a), sin(a), cos(a));
}

float mat2::Determinant(const mat2& m) {
  return m.xx * m.yy - m.xy * m.yx;
}

void mat2::Print(const mat2& m) {
  printf("| %0.2f, %0.2f |\n", m.xx, m.xy);
  printf("| %0.2f, %0.2f |\n", m.yx, m.yy);
}

mat2 mat2::operator*(const mat2& m) const {
  return mat2(xx * m.xx + xy * m.yx, xx * m.xy + xy * m.yy,
                    yx * m.xx + yy * m.yx, yx * m.xy + yy * m.yy);
}

vec2 mat2::operator*(const vec2& v) const {
  return vec2(v.x * xx + v.y * xy, v.x * yx + v.y * yy);
}

mat2 mat2::Inverse(const mat2& m) {
  float det = mat2::Determinant(m);
  if (det == 0) {
    printf("[ERROR]: Cannot Invert non-singular 2x2 matrix.\n");
    exit(EXIT_FAILURE);
  }
  float fac = 1.0 / det;

  return mat2(fac * m.yy, fac * -m.xy, fac * -m.yx, fac * m.xx);
}

mat3::mat3(void) {}
mat3::mat3(float xx, float xy, float xz, float yx, float yy,
                       float yz, float zx, float zy, float zz)
    : xx(xx), xy(xy), xz(xz), yx(yx), yy(yy), yz(yz), zx(zx), zy(zy), zz(zz) {}

mat3::mat3(const vec3& r1, const vec3& r2, const vec3& r3)
    : xx(r1.x),
      xy(r1.y),
      xz(r1.z),
      yx(r2.x),
      yy(r2.y),
      yz(r2.z),
      zx(r3.x),
      zy(r3.y),
      zz(r3.z) {}

mat3 mat3::Id(void) {
  return mat3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);
}

mat3 mat3::Zero(void) {
  return mat3(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
}

mat3 mat3::RotationX(float a) {
  return mat3(1.0, 0.0, 0.0, 0.0, cos(a), -sin(a), 0.0, sin(a), cos(a));
}

mat3 mat3::RotationY(float a) {
  return mat3(cos(a), 0.0, sin(a), 0.0, 1.0, 0.0, -sin(a), 0.0, cos(a));
}

mat3 mat3::RotationZ(float a) {
  return mat3(cos(a), -sin(a), 0.0, sin(a), cos(a), 0.0, 0.0, 0.0, 1.0);
}

mat3 mat3::RotationAngleAxis(const vec3& v, float angle) {
  float c = cos(angle);
  float s = sin(angle);
  float nc = 1 - c;

  return mat3(
      v.x * v.x * nc + c, v.x * v.y * nc - v.z * s, v.x * v.z * nc + v.y * s,
      v.y * v.x * nc + v.z * s, v.y * v.y * nc + c, v.y * v.z * nc - v.x * s,
      v.z * v.x * nc - v.y * s, v.z * v.y * nc + v.x * s, v.z * v.z * nc + c);
}

void mat3::Print(const mat3& m) {
  printf("| %0.2f, %0.2f, %0.2f |\n", m.xx, m.xy, m.xz);
  printf("| %0.2f, %0.2f, %0.2f |\n", m.yx, m.yy, m.yz);
  printf("| %0.2f, %0.2f, %0.2f |\n", m.zx, m.zy, m.zz);
}

mat3 mat3::operator*(const mat3& m) const {
  return mat3((xx * m.xx) + (xy * m.yx) + (xz * m.zx),
                    (xx * m.xy) + (xy * m.yy) + (xz * m.zy),
                    (xx * m.xz) + (xy * m.yz) + (xz * m.zz),

                    (yx * m.xx) + (yy * m.yx) + (yz * m.zx),
                    (yx * m.xy) + (yy * m.yy) + (yz * m.zy),
                    (yx * m.xz) + (yy * m.yz) + (yz * m.zz),

                    (zx * m.xx) + (zy * m.yx) + (zz * m.zx),
                    (zx * m.xy) + (zy * m.yy) + (zz * m.zy),
                    (zx * m.xz) + (zy * m.yz) + (zz * m.zz));
}

vec3 mat3::operator*(const vec3& v) const {
  return vec3((xx * v.x) + (xy * v.y) + (xz * v.z),
                 (yx * v.x) + (yy * v.y) + (yz * v.z),
                 (zx * v.x) + (zy * v.y) + (zz * v.z));
}

float mat3::Determinant(const mat3& m) {
  return (m.xx * m.yy * m.zz) + (m.xy * m.yz * m.zx) + (m.xz * m.yx * m.zy) -
         (m.xz * m.yy * m.zx) - (m.xy * m.yx * m.zz) - (m.xx * m.yz * m.zy);
}

mat3 mat3::Inverse(const mat3& m) {
  float det = mat3::Determinant(m);
  if (det == 0) {
    printf("[ERROR]: Cannot Invert non-singular 3x3 matrix.\n");
    exit(EXIT_FAILURE);
  }

  float fac = 1.0 / det;

  return mat3(
      fac * mat2::Determinant(mat2(m.yy, m.yz, m.zy, m.zz)),
      fac * mat2::Determinant(mat2(m.xz, m.xy, m.zz, m.zy)),
      fac * mat2::Determinant(mat2(m.xy, m.xz, m.yy, m.yz)),

      fac * mat2::Determinant(mat2(m.yz, m.yx, m.zz, m.zx)),
      fac * mat2::Determinant(mat2(m.xx, m.xz, m.zx, m.zz)),
      fac * mat2::Determinant(mat2(m.xz, m.xx, m.yz, m.yx)),

      fac * mat2::Determinant(mat2(m.yx, m.yy, m.zx, m.zy)),
      fac * mat2::Determinant(mat2(m.xy, m.xx, m.zy, m.zx)),
      fac * mat2::Determinant(mat2(m.xx, m.xy, m.yx, m.yy)));
}

mat4::mat4(void) {}
mat4::mat4(float xx, float xy, float xz, float xw, float yx,
                       float yy, float yz, float yw, float zx, float zy,
                       float zz, float zw, float wx, float wy, float wz,
                       float ww)
    : xx(xx),
      xy(xy),
      xz(xz),
      xw(xw),
      yx(yx),
      yy(yy),
      yz(yz),
      yw(yw),
      zx(zx),
      zy(zy),
      zz(zz),
      zw(zw),
      wx(wx),
      wy(wy),
      wz(wz),
      ww(ww) {}

mat4::mat4(const vec4& r1, const vec4& r2, const vec4& r3,
                       const vec4& r4)
    : xx(r1.x),
      xy(r1.y),
      xz(r1.z),
      xw(r1.w),
      yx(r2.x),
      yy(r2.y),
      yz(r2.z),
      yw(r2.w),
      zx(r3.x),
      zy(r3.y),
      zz(r3.z),
      zw(r3.w),
      wx(r4.x),
      wy(r4.y),
      wz(r4.z),
      ww(r4.w) {}

mat4 mat4::Id(void) {
  return mat4(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
                    0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::Zero(void) {
  return mat4(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0);
}

mat4 mat4::RotationX(float a) {
  return mat4(1.0, 0.0, 0.0, 0.0, 0.0, cos(a), -sin(a), 0.0, 0.0, sin(a),
                    cos(a), 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::RotationY(float a) {
  return mat4(cos(a), 0.0, sin(a), 0.0, 0.0, 1.0, 0.0, 0.0, -sin(a), 0.0,
                    cos(a), 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::RotationZ(float a) {
  return mat4(cos(a), -sin(a), 0.0, 0.0, sin(a), cos(a), 0.0, 0.0, 0.0,
                    0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::RotationEuler(float a, float b, float c) {
  return mat4(
      cos(b) * cos(c), cos(b) * sin(c), -sin(b), 0.0,

      -cos(a) * sin(c) + sin(a) * sin(b) * cos(c),
      cos(a) * cos(c) + sin(a) * sin(b) * sin(c), sin(a) * cos(b), 0.0,

      sin(a) * sin(c) + cos(a) * sin(b) * cos(c),
      -sin(a) * cos(c) + cos(a) * sin(b) * sin(c), cos(a) * cos(b), 0.0,

      0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::RotationAngleAxis(const vec3& v, float angle) {
  float c = cos(angle);
  float s = sin(angle);
  float nc = 1 - c;

  return mat4(v.x * v.x * nc + c, v.x * v.y * nc - v.z * s,
                    v.x * v.z * nc + v.y * s, 0.0, v.y * v.x * nc + v.z * s,
                    v.y * v.y * nc + c, v.y * v.z * nc - v.x * s, 0.0,
                    v.z * v.x * nc - v.y * s, v.z * v.y * nc + v.x * s,
                    v.z * v.z * nc + c, 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::Translation(const vec3& trans) {
  return mat4(1.0, 0.0, 0.0, trans.x, 0.0, 1.0, 0.0, trans.y, 0.0, 0.0,
                    1.0, trans.z, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::Scale(const vec3& scale) {
  return mat4(scale.x, 0.0, 0.0, 0.0, 0.0, scale.y, 0.0, 0.0, 0.0, 0.0,
                    scale.z, 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat4 mat4::ViewLookAt(const vec3& position,
                                  const vec3& target, const vec3& up) {
  vec3 zaxis = vec3::Normalize(target - position);
  vec3 xaxis = vec3::Normalize(vec3::Cross(up, zaxis));
  vec3 yaxis = vec3::Cross(zaxis, xaxis);

  mat4 view_matrix =
      mat4(xaxis.x, xaxis.y, xaxis.z, 0.0, yaxis.x, yaxis.y, yaxis.z, 0.0,
                 -zaxis.x, -zaxis.y, -zaxis.z, 0.0, 0.0, 0.0, 0.0, 1.0);

  view_matrix = view_matrix * mat4::Translation(-position);

  return view_matrix;
}

mat4 mat4::Perspective(float fov, float near_clip, float far_clip,
                                   float ratio) {
  float right, left, bottom, top;

  right = -(near_clip * tanf(fov));
  left = -right;

  top = ratio * near_clip * tanf(fov);
  bottom = -top;

  return mat4((2.0 * near_clip) / (right - left), 0.0,
                    (right + left) / (right - left), 0.0, 0.0,
                    (2.0 * near_clip) / (top - bottom),
                    (top + bottom) / (top - bottom), 0.0, 0.0, 0.0,
                    (-far_clip - near_clip) / (far_clip - near_clip),
                    (-(2.0 * near_clip) * far_clip) / (far_clip - near_clip),
                    0.0, 0.0, -1.0, 0.0);
}

mat4 mat4::Orthographic(float left, float right, float bottom,
                                    float top, float near, float far) {
  return mat4(
      2 / (right - left), 0.0, 0.0, -(right + left) / (right - left), 0.0,
      2 / (top - bottom), 0.0, -(top + bottom) / (top - bottom), 0.0, 0.0,
      -2 / (far - near), -(far + near) / (far - near), 0.0, 0.0, 0.0, 1.0);
}

void mat4::Print(const mat4& m) {
  printf("| %2.0f, %2.0f, %2.0f, %2.0f |\n", m.xx, m.xy, m.xz, m.xw);
  printf("| %2.0f, %2.0f, %2.0f, %2.0f |\n", m.yx, m.yy, m.yz, m.yw);
  printf("| %2.0f, %2.0f, %2.0f, %2.0f |\n", m.zx, m.zy, m.zz, m.zw);
  printf("| %2.0f, %2.0f, %2.0f, %2.0f |\n", m.wx, m.wy, m.wz, m.ww);
}

mat4 mat4::FromMat3(const mat3& m) {
  return mat4(m.xx, m.xy, m.xz, 0.0, m.yx, m.yy, m.yz, 0.0, m.zx, m.zy,
                    m.zz, 0.0, 0.0, 0.0, 0.0, 1.0);
}

mat3 mat4::ToMat3(const mat4& m) {
  return mat3(m.xx, m.xy, m.xz, m.yx, m.yy, m.yz, m.zx, m.zy, m.zz);
}

mat4 mat4::operator*(const mat4& m) const {
  return mat4(

      (xx * m.xx) + (xy * m.yx) + (xz * m.zx) + (xw * m.wx),
      (xx * m.xy) + (xy * m.yy) + (xz * m.zy) + (xw * m.wy),
      (xx * m.xz) + (xy * m.yz) + (xz * m.zz) + (xw * m.wz),
      (xx * m.xw) + (xy * m.yw) + (xz * m.zw) + (xw * m.ww),

      (yx * m.xx) + (yy * m.yx) + (yz * m.zx) + (yw * m.wx),
      (yx * m.xy) + (yy * m.yy) + (yz * m.zy) + (yw * m.wy),
      (yx * m.xz) + (yy * m.yz) + (yz * m.zz) + (yw * m.wz),
      (yx * m.xw) + (yy * m.yw) + (yz * m.zw) + (yw * m.ww),

      (zx * m.xx) + (zy * m.yx) + (zz * m.zx) + (zw * m.wx),
      (zx * m.xy) + (zy * m.yy) + (zz * m.zy) + (zw * m.wy),
      (zx * m.xz) + (zy * m.yz) + (zz * m.zz) + (zw * m.wz),
      (zx * m.xw) + (zy * m.yw) + (zz * m.zw) + (zw * m.ww),

      (wx * m.xx) + (wy * m.yx) + (wz * m.zx) + (ww * m.wx),
      (wx * m.xy) + (wy * m.yy) + (wz * m.zy) + (ww * m.wy),
      (wx * m.xz) + (wy * m.yz) + (wz * m.zz) + (ww * m.wz),
      (wx * m.xw) + (wy * m.yw) + (wz * m.zw) + (ww * m.ww)

          );
}

mat4 mat4::operator*(const mat3& m) const {
  mat4 m2 = mat4::FromMat3(m);
  return *this * m2;
}

vec4 mat4::operator*(const vec4& v) const {
  return vec4((xx * v.x) + (xy * v.y) + (xz * v.z) + (xw * v.w),
                 (yx * v.x) + (yy * v.y) + (yz * v.z) + (yw * v.w),
                 (zx * v.x) + (zy * v.y) + (zz * v.z) + (zw * v.w),
                 (wx * v.x) + (wy * v.y) + (wz * v.z) + (ww * v.w));
}

vec3 mat4::operator*(const vec3& vec) const {
  vec4 v = vec4::ToHomogeneous(vec);

  return vec4::FromHomogeneous(
      vec4((xx * v.x) + (xy * v.y) + (xz * v.z) + (xw * v.w),
              (yx * v.x) + (yy * v.y) + (yz * v.z) + (yw * v.w),
              (zx * v.x) + (zy * v.y) + (zz * v.z) + (zw * v.w),
              (wx * v.x) + (wy * v.y) + (wz * v.z) + (ww * v.w)));
}

mat4 mat4::Transpose(const mat4& m) {
  return mat4(m.xx, m.yx, m.zx, m.wx, m.xy, m.yy, m.zy, m.wy, m.xz, m.yz,
                    m.zz, m.wz, m.xw, m.yw, m.zw, m.ww);
}

float mat4::Determinant(const mat4& m) {
  float cofact_xx = mat3::Determinant(
      mat3(m.yy, m.yz, m.yw, m.zy, m.zz, m.zw, m.wy, m.wz, m.ww));
  float cofact_xy = -mat3::Determinant(
      mat3(m.yx, m.yz, m.yw, m.zx, m.zz, m.zw, m.wx, m.wz, m.ww));
  float cofact_xz = mat3::Determinant(
      mat3(m.yx, m.yy, m.yw, m.zx, m.zy, m.zw, m.wx, m.wy, m.ww));
  float cofact_xw = -mat3::Determinant(
      mat3(m.yx, m.yy, m.yz, m.zx, m.zy, m.zz, m.wx, m.wy, m.wz));

  return (cofact_xx * m.xx) + (cofact_xy * m.xy) + (cofact_xz * m.xz) +
         (cofact_xw * m.xw);
}

mat4 mat4::Inverse(const mat4& m) {
  float det = mat4::Determinant(m);
  if (det == 0) {
    printf("[ERROR]: Cannot Invert non-singular 4x4 matrix.\n");
    exit(EXIT_FAILURE);
  }

  float fac = 1.0 / det;

  mat4 ret;
  ret.xx = fac * mat3::Determinant(mat3(
                     m.yy, m.yz, m.yw, m.zy, m.zz, m.zw, m.wy, m.wz, m.ww));
  ret.xy = fac *
           -mat3::Determinant(mat3(m.yx, m.yz, m.yw, m.zx, m.zz,
                                               m.zw, m.wx, m.wz, m.ww));
  ret.xz = fac * mat3::Determinant(mat3(
                     m.yx, m.yy, m.yw, m.zx, m.zy, m.zw, m.wx, m.wy, m.ww));
  ret.xw = fac *
           -mat3::Determinant(mat3(m.yx, m.yy, m.yz, m.zx, m.zy,
                                               m.zz, m.wx, m.wy, m.wz));

  ret.yx = fac *
           -mat3::Determinant(mat3(m.xy, m.xz, m.xw, m.zy, m.zz,
                                               m.zw, m.wy, m.wz, m.ww));
  ret.yy = fac * mat3::Determinant(mat3(
                     m.xx, m.xz, m.xw, m.zx, m.zz, m.zw, m.wx, m.wz, m.ww));
  ret.yz = fac *
           -mat3::Determinant(mat3(m.xx, m.xy, m.xw, m.zx, m.zy,
                                               m.zw, m.wx, m.wy, m.ww));
  ret.yw = fac * mat3::Determinant(mat3(
                     m.xx, m.xy, m.xz, m.zx, m.zy, m.zz, m.wx, m.wy, m.wz));

  ret.zx = fac * mat3::Determinant(mat3(
                     m.xy, m.xz, m.xw, m.yy, m.yz, m.yw, m.wy, m.wz, m.ww));
  ret.zy = fac *
           -mat3::Determinant(mat3(m.xx, m.xz, m.xw, m.yx, m.yz,
                                               m.yw, m.wx, m.wz, m.ww));
  ret.zz = fac * mat3::Determinant(mat3(
                     m.xx, m.xy, m.xw, m.yx, m.yy, m.yw, m.wx, m.wy, m.ww));
  ret.zw = fac *
           -mat3::Determinant(mat3(m.xx, m.xy, m.xz, m.yx, m.yy,
                                               m.yz, m.wx, m.wy, m.wz));

  ret.wx = fac *
           -mat3::Determinant(mat3(m.xy, m.xz, m.xw, m.yy, m.yz,
                                               m.yw, m.zy, m.zz, m.zw));
  ret.wy = fac * mat3::Determinant(mat3(
                     m.xx, m.xz, m.xw, m.yx, m.yz, m.yw, m.zx, m.zz, m.zw));
  ret.wz = fac *
           -mat3::Determinant(mat3(m.xx, m.xy, m.xw, m.yx, m.yy,
                                               m.yw, m.zx, m.zy, m.zw));
  ret.ww = fac * mat3::Determinant(mat3(
                     m.xx, m.xy, m.xz, m.yx, m.yy, m.yz, m.zx, m.zy, m.zz));

  ret = mat4::Transpose(ret);

  return ret;
}

mat4 mat4::operator*(float fac) const {
  return mat4(xx * fac, xy * fac, xz * fac, xw * fac, yx * fac, yy * fac,
                    yz * fac, yw * fac, zx * fac, zy * fac, zz * fac, zw * fac,
                    wx * fac, wy * fac, wz * fac, ww * fac);
}

mat4 mat4::operator+(const mat4& m) const {
  return mat4(xx + m.xx, xy + m.xy, xz + m.xz, xw + m.xw, yx + m.yx,
                    yy + m.yy, yz + m.yz, yw + m.yw, zx + m.zx, zy + m.zy,
                    zz + m.zz, zw + m.zw, wx + m.wx, wy + m.wy, wz + m.wz,
                    ww + m.ww);
}
