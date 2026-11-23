#version 150

uniform vec2 Size;
uniform float Time;

in vec4 vertexColor;
out vec4 fragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / Size;
    fragColor = vec4(uv.x, uv.y, sin(Time / 5) / 2 + 0.5, 1);
}