data_file = readtable('datasets/combined.csv');
matrix = table2array(data_file);
matrix = matrix(randsample(1:length(matrix),length(matrix)),:);

[rows,columns] = size(matrix);

training_data_size = rows * 0.8;
training_data_size = floor(training_data_size);

train_data = matrix(1:training_data_size, :);
test_data = matrix(training_data_size + 1:rows, :);

Length = train_data(:, end);
Ylabels = train_data(:,end);
train_data(:,end) = [];

testAxisX = test_data;
testAxisX(:, end) = [];

testAxisY = test_data(:, end);

tic
mdl = fitcsvm(...
    train_data, ...
    Ylabels, ...
    'KernelFunction', 'linear', ...
    'PolynomialOrder', [], ...
    'KernelScale', 'auto', ...
    'BoxConstraint', 1, ...
    'Standardize', true, ...
    'ClassNames', [0; 1]);
dataLabel = predict(mdl, testAxisX);
toc
index = testAxisY == dataLabel;
correct_classifications = nnz(index);
accuracy = (correct_classifications/length(dataLabel)) * 100;
display(accuracy);