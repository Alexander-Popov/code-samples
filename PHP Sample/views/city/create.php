<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;
use app\Helper\View\CityViewHelper;

/* @var $this yii\web\View */
/* @var $form yii\widgets\ActiveForm */

/* @var $this yii\web\View */

$this->title = 'Сздать город';
?>

<h1><?= Html::encode($this->title) ?></h1>

<?php $form = ActiveForm::begin([
    'options' => ['class' => 'form-horizontal'],
    'fieldConfig' => [
        'template' => '{label}<div class="col-md-5">{input}{error}</div>',
        'labelOptions' => ['class' => 'col-md-2'],
    ],
]); ?>

<?= $form->field($model, 'name')->textInput(['maxlength' => true]) ?>

<?= $form->field($model, 'country')->dropDownList(CityViewHelper::getCountriesArray()) ?>

<?= $form->field($model, 'extraCharge')->textInput(['maxlength' => true]) ?>

<?= Html::submitButton('Создать', ['class' => 'btn btn-success']) ?>

<?php ActiveForm::end(); ?>
