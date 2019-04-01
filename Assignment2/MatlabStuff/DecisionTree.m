data_file = readtable('datasets/combined.csv');
matrix = table2array(data_file);
[rows,columns] = size(matrix);

training_data_size = rows * 0.8;
training_data_size = floor(training_data_size);

train_data = matrix(1:training_data_size, :);
test_data = matrix(training_data_size + 1:rows, :);

Length = train_data(:, end);
train_data(:,end) = [];

testAxisX = test_data;
testAxisX(:, end) = [];

testAxisY = test_data(:, end);

tic
Model = fitctree(train_data, Length);
dataLabel = predict(Model, testAxisX);
toc

index = testAxisY == dataLabel;
correct_classifications = nnz(index);
accuracy = (correct_classifications/length(dataLabel)) * 100;
display(accuracy);

view(Model, 'mode', 'graph');
