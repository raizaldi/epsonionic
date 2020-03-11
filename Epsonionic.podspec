
  Pod::Spec.new do |s|
    s.name = 'Epsonionic'
    s.version = '0.0.1'
    s.summary = 'this is plugin for ionic use capacitor for access epson printer network'
    s.license = 'GPL'
    s.homepage = 'git@github.com:raizaldi/epsonionic.git'
    s.author = 'raizaldi'
    s.source = { :git => 'git@github.com:raizaldi/epsonionic.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end