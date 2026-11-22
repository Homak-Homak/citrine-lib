#version 150

uniform vec2 Size;
uniform float Time;

in vec4 vertexColor;
out vec4 fragColor;

// shader from https://www.shadertoy.com/view/7ly3Wh by peabrainiac
const vec2 MINIBROT_C = vec2(-1.98542425,0.0);
const vec2 MINIBROT_SCALE = vec2(5.81884941e-5,0.0);
const vec2 MINIBROT_A = vec2(-161.347726,0.0);
const float MINIBROT_PERIOD = 5.0;

const float MINIBROT_R = 8.0;
const float MINIBROT_R2 = 32.0;

const int ITER = 500;
const float BAILOUT = 32.0;

vec2 cmul(vec2 a, vec2 b){
    return vec2(dot(a,vec2(1.0,-1.0)*b),dot(a,b.yx));
}

vec2 cdiv(vec2 a, vec2 b){
    return vec2(dot(a,b),dot(a.yx,vec2(1.0,-1.0)*b))/dot(b,b);
}

void mainImage(out vec4 fragColor, in vec2 fragCoord){
    float t = Time*Time/(Time+1.0) / 20;
    float s = -log(length(MINIBROT_SCALE));
    int n = int(ceil(t/s));
    float zoom = exp(-(t-s*float(n)));
    float theta = float(n)*atan(MINIBROT_SCALE.y,MINIBROT_SCALE.x);
    vec2 C = cmul(MINIBROT_C,cdiv(vec2(1.0,0.0),vec2(1.0,0.0)-MINIBROT_SCALE));
    vec2 dc = vec2(cos(theta),-sin(theta))*10.0*zoom/length(Size);
    vec2 c = C+cmul(dc,vec2(1.0,-1.0)*(fragCoord-Size.xy*0.5));
    while (n>0&&dot(c-MINIBROT_C,c-MINIBROT_C)>MINIBROT_R2){
        c = MINIBROT_C+cmul(c,MINIBROT_SCALE);
        dc = cmul(dc,MINIBROT_SCALE);
        n--;
    }

    vec2 z = vec2(0.0);
    vec2 dz = dc;
    int i = 0;
    float i2 = 0.0;
    float escapeRadius = n==0?BAILOUT:MINIBROT_R;
    while(i<ITER){
        if (dot(z,z)>escapeRadius){
            if (n==0){
                break;
            }else{
                z = cdiv(z,MINIBROT_A);
                dz = cdiv(dz,MINIBROT_A);
                c = MINIBROT_C+cmul(c,MINIBROT_SCALE);
                dc = cmul(dc,MINIBROT_SCALE);
                n--;
                i2 *= MINIBROT_PERIOD;
                float escapeRadius = n==0?BAILOUT:MINIBROT_R;
            }
        }
        dz = 2.0*cmul(dz,z)+dc;
        z = cmul(z,z)+c;
        i++;
        i2++;
    }

    float d = !(i<ITER)?0.0:sqrt(dot(z,z)/dot(dz,dz))*0.5*log(dot(z,z));
    fragColor = vec4(vec3((1.0-d)*(0.5+0.5*cos(log(1.0+i2*2.0e-5)))),1.0);
}

void main() {
    vec4 color;
    mainImage(color, gl_FragCoord.xy);
    fragColor = color;
}