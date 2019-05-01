data_about = dir('datasets/data/about/*.csv');
data_father = dir('datasets/data/father/*.csv');

dir_father = 'datasets/data/father';
dir_about  = 'datasets/data/about';

size_father = length(data_father);
size_about = length(data_about);

matrix = [];
for i = 1:size_father
    each = data_father(i).name;
    path = fullfile(dir_father, each);
    matrix_read = readtable(path);
    matrix = vertcat(matrix, matrix_read);
end

% Only take the indices of the columns we care about.
matrix = matrix(:,[4 5 7 8 10 11 13 14 16 17 19 20 22 23 25 26 28 29 31 32 34 35]);
[m,~] = size(matrix);
data_labels = ones(m,1);
%Assign a label of 1 to father
data_labels = array2table(data_labels);
matrix = horzcat(matrix,data_labels);

mat1 = [];

for j = 1:size_about
    each = data_about(j).name;
    path = fullfile(dir_about, each);
    matrix_read = readtable(path);
    mat1 = vertcat(mat1, matrix_read);
end

mat1 = mat1(:,[4 5 7 8 10 11 13 14 16 17 19 20 22 23 25 26 28 29 31 32 34 35]);
[m,n] = size(mat1);
%Assign a label of 0 to about
data_labels = zeros(m,1);
data_labels = array2table(data_labels);
mat1 = horzcat(mat1, data_labels);

matrix = vertcat(mat1, matrix);
writetable(matrix,'datasets/combined.csv');