#!/usr/bin/env python3

import os
import sys
import zipfile
import shutil
import subprocess

src_files = {
    'src_path': 'src/main/java/',
    'dest_path': 'src/',
    'include': [ x+'.java' for x in
        ('Block', 'BlockWorldException', 'Builder', 'GrassBlock',
        'GroundBlock', 'InvalidBlockException', 'NoExitException',
        'SoilBlock', 'StoneBlock', 'Tile', 'TooHighException',
        'TooLowException', 'WoodBlock')
    ]
}

test_files = {
    'src_path': 'src/test/java/',
    'dest_path': 'test/',
    'include': [
        'GrassBlockTest.java', 'TileTest.java'
    ]
}

def main():
    print('Starting artifact build...')
    print('Copying to temp directory...')
    #shutil.copytree('.', './__temp')
    #os.chdir('./__temp')

    os.makedirs('./__temp', exist_ok=True)

    cp = ['.']
    test_dir = os.path.join('./__temp', test_files['dest_path'])
    for config in (src_files, test_files):
        java_src = []

        d = os.path.join('./__temp', config['dest_path'])
        os.makedirs(d, exist_ok=True)
        for f in config['include']:
            s = os.path.join(config['src_path'], f)
            print('Copying', s, 'to', d, '...')
            shutil.copy2(s, os.path.normpath(d))
            java_src.append(os.path.normpath(os.path.join(d, f)))

        print('Compiling directory', d)
        subprocess.check_call([r'C:\Program Files\Java\jdk1.8.0_181\bin\javac.exe', '-cp', os.sep.join(cp)]+java_src)
        cp.append(d)

    test_classes = (x for x in os.listdir(test_dir) if x.endswith('.java') and os.path.isfile(x))

    print('Running tests...')
    subprocess.check_call(
        ['java -cp '+os.sep.join(cp)+' org.junit.runner.JUnitCore '+' '.join(test_classes)+'/*'])

    print('Executing tests...')
    test_result = subprocess.check_call(['mvn clean test -B'], shell=True)

    print('Compiling artifact zip...')

    if len(sys.argv) < 2:
        print('Requires zip name argument.')
        sys.exit(1)

    zf = zipfile.ZipFile('./../'+sys.argv[1], 'w')

    for file_structure in (src_files, test_files):
        for f in file_structure['include']:
            print('    Adding', file_structure['src_path']+f)
            zf.write(file_structure['src_path']+f, file_structure['dest_path']+f)
    print('Removing temp directory...')
    os.chdir('..')
    shutil.rmtree('__temp')
    print('Done.')





if __name__ == '__main__':
    main()