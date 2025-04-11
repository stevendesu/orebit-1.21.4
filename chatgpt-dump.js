const fs = require('fs');

const prefix = './src/main/java/com/orebit/mod/worldmodel'

let str = "";
function print(newStr) {
    str += newStr + '\n';
}

function recursivePrint(dir) {
    const files = fs.readdirSync(dir);
    for (file of files) {
        const printName = (dir + '/' + file).replace(prefix, '');
        if (fs.statSync(dir + '/' + file).isDirectory()) {
            print('Directory: ' + printName);
            print('------------------');
            recursivePrint(dir + '/' + file);
        } else if( file.endsWith('.java')) {
            print('File: ' + printName);
            const filePath = dir + '/' + file;
            const content = fs.readFileSync(filePath, 'utf8');
            print(content);
            print('------------------');
        }
    }
}

recursivePrint(prefix);

fs.writeFileSync('dump.txt', str, 'utf8');
